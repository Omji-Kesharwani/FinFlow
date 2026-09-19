package wallet_service.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wallet_service.dto.WalletTransactionResponse;
import wallet_service.entity.TransactionType;
import wallet_service.entity.Wallet;
import wallet_service.entity.WalletStatus;
import wallet_service.entity.WalletTransaction;
import wallet_service.exception.*;
import wallet_service.repository.WalletRepository;
import wallet_service.repository.WalletTransactionRepository;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class WalletService {

    private final WalletRepository walletRepository ;
    private final WalletTransactionRepository walletTransactionRepository ;


    public WalletService(WalletRepository walletRepository, WalletTransactionRepository walletTransactionRepository) {
        this.walletRepository = walletRepository;
        this.walletTransactionRepository = walletTransactionRepository;
    }

    @Transactional
    public Wallet createWallet(UUID userId){
        if(walletRepository.existsByUserId(userId))
        {
            throw new WalletAlreadyExistsException(
                    "Wallet already exists for this user"
            );
        }

        OffsetDateTime now = OffsetDateTime.now() ;

        Wallet wallet = new Wallet(
                userId,
                "INR",
                BigDecimal.ZERO,
                WalletStatus.ACTIVE,
                now,
                now
        ) ;

       return walletRepository.save(wallet) ;


    }

    @Transactional
    public  Wallet getWallet(UUID userId){
       return getWalletOrThrow(userId) ;

    }

    @Transactional
    public Wallet deposit(UUID userId , BigDecimal amount){

        System.out.println("DEPOSIT SERVICE CALLED");
        Wallet wallet = getWalletOrThrow(userId) ;
        validateWalletIsActive(wallet);

        BigDecimal newBalance = wallet.getBalance().add(amount) ;

        wallet.setBalance(newBalance);

        wallet.setUpdatedAt(OffsetDateTime.now());

        walletRepository.save(wallet) ;

        System.out.println("TRANSACTION CREATED");
        WalletTransaction transaction = createTransaction(
                wallet,
                TransactionType.DEPOSIT,
                amount,
                newBalance,
        "DEP-"+UUID.randomUUID()
        );
        walletTransactionRepository.save(transaction) ;

        System.out.println("TRANSACTION SAVED");

        return wallet ;

    }

    @Transactional
    public Wallet withdraw(UUID userId , BigDecimal amount){
        Wallet wallet = getWalletOrThrow(userId) ;
        validateWalletIsActive(wallet);

        if(wallet.getBalance().compareTo(amount) < 0)
        {
            throw new InsufficientBalanceException(
                    "Insufficient wallet balance"
            );
        }

       BigDecimal newBalance = wallet.getBalance().subtract(amount);

        wallet.setBalance(newBalance);
        wallet.setUpdatedAt(OffsetDateTime.now());

        walletRepository.save(wallet) ;

        WalletTransaction transaction = createTransaction(
                wallet,
                TransactionType.WITHDRAWAL,
                amount,
                newBalance,
        "WD-"+UUID.randomUUID());

        walletTransactionRepository.save(transaction) ;

        return wallet ;

    }

    @Transactional
    public Wallet freezeWallet(UUID userId){
        Wallet wallet = getWalletOrThrow(userId) ;

        if(wallet.getStatus() == WalletStatus.FROZEN)
        {
            return wallet ;
        }

        wallet.setStatus(WalletStatus.FROZEN);
        wallet.setUpdatedAt(OffsetDateTime.now());

        Wallet savedWallet = walletRepository.save(wallet) ;
        return wallet ;

    }

    @Transactional
    public Wallet unfreezeWallet(UUID userId){
        Wallet wallet = getWalletOrThrow(userId) ;

        if(wallet.getStatus() == WalletStatus.ACTIVE)
        {
            return wallet ;
        }

        wallet.setStatus(WalletStatus.ACTIVE);
        wallet.setUpdatedAt(OffsetDateTime.now());

        Wallet savedWallet = walletRepository.save(wallet);

        return  wallet ;
    }

    @Transactional(readOnly = true)
    public Page<WalletTransactionResponse> getTransactions(UUID userId , Pageable pageable){
        Wallet wallet = getWalletOrThrow(userId);
        return walletTransactionRepository
                .findByWalletIdOrderByCreatedAtDesc(wallet.getId(),pageable)
                .map(WalletTransactionResponse::from);
    }

    @Transactional
    public void transfer(UUID senderUserId,UUID receiverUserId,BigDecimal amount,String reference)
    {
        if (reference == null || reference.isBlank()) {
            throw new InvalidTransferException(
                    "Transfer reference is required"
            );
        }

        if(walletTransactionRepository.existsByReference(reference))
        {
            return ;
        }

         if(senderUserId.equals(receiverUserId))
         {
             throw  new InvalidTransferException( "Sender and receiver wallets cannot be the same");
         }

         if( amount == null || amount.compareTo(BigDecimal.ZERO) <= 0)
         {
             throw new InvalidTransferException("Transfer amount must be greater than zero");
         }

         Wallet sender = getWalletOrThrow(senderUserId);
         Wallet receiver = getWalletOrThrow(receiverUserId);

         validateWalletIsActive(sender);
         validateWalletIsActive(receiver);

         if(!sender.getCurrency().equals(receiver.getCurrency()))
         {
             throw new InvalidTransferException(
                     "Sender and receiver currencies must match"
             );
         }

         if(sender.getBalance().compareTo(amount) < 0)
         {
             throw new InsufficientBalanceException(
                     "Insufficient wallet balance "
             );
         }

         BigDecimal senderNewBalance = sender.getBalance().subtract(amount);
         sender.setBalance(senderNewBalance);
         sender.setUpdatedAt(OffsetDateTime.now());

         BigDecimal receiverNewBalance = receiver.getBalance().add(amount);

         receiver.setBalance(receiverNewBalance);
         receiver.setUpdatedAt(OffsetDateTime.now());

         walletRepository.save(sender);
         walletRepository.save(receiver);



         WalletTransaction creditTransaction = createTransaction(
                 receiver,
                 TransactionType.CREDIT,
                 amount,
                 receiverNewBalance,
                 reference
         );

         WalletTransaction debitTransaction = createTransaction(
                 sender,
                 TransactionType.DEBIT,
                 amount,
                 senderNewBalance,
                 reference
         );

         walletTransactionRepository.save(debitTransaction);
         walletTransactionRepository.save(creditTransaction);
    }

    private void validateWalletIsActive(Wallet wallet){

        if(wallet.getStatus() != WalletStatus.ACTIVE)
        {
            throw new WalletNotActiveException(
                    "Wallet is not active"
            );
        }
    }

    private Wallet getWalletOrThrow(UUID userId)
    {
       return walletRepository.findByUserId(userId)
                .orElseThrow(()-> new WalletNotFoundException(
                        "Wallet not found for this user"
                ));
    }

    private WalletTransaction createTransaction(
            Wallet wallet ,
            TransactionType type ,
            BigDecimal amount ,
            BigDecimal balanceAfter,
            String reference
    ){
        return new WalletTransaction(
                wallet.getId(),
                type,
                amount,
                balanceAfter,
                OffsetDateTime.now(),
                reference
        );
    }






}
