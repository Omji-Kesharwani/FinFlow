package payment_service.service;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import payment_service.client.WalletClient;
import payment_service.dto.TransferDto;
import payment_service.entity.Transfer;
import payment_service.entity.TransferStatus;
import payment_service.exception.IdempotencyKeyConflictException;
import payment_service.exception.InvalidTransferException;
import payment_service.exception.WalletOperationException;
import payment_service.exception.WalletServiceException;
import payment_service.repository.TransferRepository;

import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class TransferService {

    private final TransferRepository transferRepository;
    private final WalletClient walletClient;

    public TransferService(
            TransferRepository transferRepository,
            WalletClient walletClient
    ) {
        this.transferRepository = transferRepository;
        this.walletClient = walletClient;
    }

    public Transfer createTransfer(UUID senderUserId, String token , String idempotencyKey , TransferDto request)
    {
        Optional<Transfer> existingTransfer = transferRepository.findByIdempotencyKey(idempotencyKey);

        if(existingTransfer.isPresent())
        {
            Transfer transfer = existingTransfer.get() ;

            if(!transfer.getSenderUserId().equals(senderUserId) || !transfer.getReceiverUserId().equals(request.getReceiverUserId()) || transfer.getAmount().compareTo(request.getAmount()) != 0 ||
              !transfer.getCurrency().equalsIgnoreCase(request.getCurrency()))
            {
                throw new IdempotencyKeyConflictException(
                        "Idempotency-Key has already been used for a different transfer"
                );
            }

            return transfer ;
        }

        if(senderUserId.equals(request.getReceiverUserId()))
        {
            throw new InvalidTransferException(
                    "Sender and receiver cannot be the same user"
            );
        }

        String reference = generateReference() ;

        OffsetDateTime now = OffsetDateTime.now() ;

        Transfer transfer = new Transfer(
                senderUserId,
                request.getReceiverUserId(),
                request.getAmount(),
                request.getCurrency(),
                TransferStatus.PENDING,
                reference,
                idempotencyKey,
                now,
                now
        );

        Transfer savedTransfer;

        try {
            savedTransfer = transferRepository.saveAndFlush(transfer);
        } catch (DataIntegrityViolationException ex) {

            Transfer existing = transferRepository
                    .findByIdempotencyKey(idempotencyKey)
                    .orElseThrow(() -> ex);

            if (!existing.getSenderUserId().equals(senderUserId)
                    || !existing.getReceiverUserId().equals(request.getReceiverUserId())
                    || existing.getAmount().compareTo(request.getAmount()) != 0
                    || !existing.getCurrency().equalsIgnoreCase(request.getCurrency())) {

                throw new IdempotencyKeyConflictException(
                        "Idempotency-Key has already been used for a different transfer"
                );
            }

            return existing;
        }

        try{
            walletClient.transfer(
                    token,
                    request.getReceiverUserId(),
                    request.getAmount(),
                    reference
            );

            savedTransfer.setStatus(TransferStatus.COMPLETED);
            savedTransfer.setUpdatedAt(OffsetDateTime.now());

            savedTransfer = transferRepository.save(savedTransfer);
        }

        catch (WalletOperationException ex)
        {
            savedTransfer.setStatus(TransferStatus.FAILED);
            savedTransfer.setUpdatedAt(OffsetDateTime.now());
            transferRepository.save(savedTransfer);

            throw ex ;
        }
        catch (WalletServiceException ex)
        {
            savedTransfer.setStatus(TransferStatus.FAILED);
            savedTransfer.setUpdatedAt(OffsetDateTime.now());

            transferRepository.save(savedTransfer);

            throw ex;

        }

        return  savedTransfer ;


    }

    private String generateReference(){

        return "TRF-" +
                UUID.randomUUID()
                        .toString()
                        .replace("-", "")
                        .substring(0, 12)
                        .toUpperCase();
    }

}
