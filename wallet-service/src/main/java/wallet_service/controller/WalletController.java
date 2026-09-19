package wallet_service.controller;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import wallet_service.dto.*;
import wallet_service.entity.Wallet;
import wallet_service.service.WalletService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/wallets")
public class WalletController {

    private final WalletService walletService ;

    public WalletController(WalletService walletService) {
        this.walletService = walletService;
    }

    @PostMapping
    public ResponseEntity<WalletResponse> createWallet(Authentication authentication){
        UUID userId = (UUID) authentication.getPrincipal() ;
        Wallet wallet = walletService.createWallet(userId);

        return ResponseEntity.status(HttpStatus.CREATED)
                        .body(WalletResponse.from(wallet));
    }

    @GetMapping
    public ResponseEntity<WalletResponse> getWallet(Authentication authentication){
        UUID userId = (UUID) authentication.getPrincipal() ;
        Wallet wallet = walletService.getWallet(userId) ;

        return ResponseEntity.ok(
                WalletResponse.from(wallet)
        );
    }

    @PostMapping("/deposit")
    public ResponseEntity<WalletResponse> deposit(Authentication authentication, @Valid @RequestBody MoneyRequest request){
        UUID userId = (UUID) authentication.getPrincipal() ;
        Wallet wallet = walletService.deposit(userId,request.amount());

        return ResponseEntity.ok(WalletResponse.from(wallet)) ;
    }

    @PostMapping("/withdraw")
    public ResponseEntity<WalletResponse> withdraw(Authentication authentication , @Valid @RequestBody MoneyRequest request){
        UUID userId = (UUID) authentication.getPrincipal() ;
        Wallet wallet = walletService.withdraw(userId,request.amount());

        return ResponseEntity.ok(WalletResponse.from(wallet)) ;
    }

    @PutMapping("/freeze")
    public ResponseEntity<WalletResponse> freezeWallet(Authentication authentication)
    {
        UUID userId = (UUID) authentication.getPrincipal() ;

        Wallet wallet = walletService.freezeWallet(userId);

        return ResponseEntity.ok(WalletResponse.from(wallet));
    }

    @PutMapping("/unfreeze")
    public ResponseEntity<WalletResponse> unfreezeWallet(Authentication authentication)
    {
        UUID userId = (UUID) authentication.getPrincipal() ;

        Wallet wallet = walletService.unfreezeWallet(userId);

        return ResponseEntity.ok(WalletResponse.from(wallet));
    }

    @GetMapping("/transactions")
    public ResponseEntity<Page<WalletTransactionResponse>> getTransactions(Authentication authentication , @Valid @ModelAttribute PageRequestDto pageRequest){

//        System.out.println("Entered into the Transaction") ;
        UUID userId = (UUID) authentication.getPrincipal() ;

        Pageable pageable = PageRequest.of(
                pageRequest.page(),
                pageRequest.size()) ;
        Page<WalletTransactionResponse> transactions = walletService.getTransactions(userId , pageable);

        return ResponseEntity.ok(transactions);
    }

    @PostMapping("/transfer")
    public ResponseEntity<String> transfer(Authentication authentication , @Valid @RequestBody TransferDto request)
    {
        UUID senderUserId = (UUID) authentication.getPrincipal() ;
        walletService.transfer(
                senderUserId,
                request.getReceiverUserId(),
                request.getAmount(),
                request.getReference()
        );

        return ResponseEntity.ok("Transfer successful");
    }
}

