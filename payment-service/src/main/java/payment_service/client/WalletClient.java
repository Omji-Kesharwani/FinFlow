package payment_service.client;

import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import payment_service.dto.WalletMoneyRequest;
import payment_service.dto.WalletTransferRequest;
import payment_service.exception.WalletOperationException;
import payment_service.exception.WalletServiceException;
import tools.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.util.UUID;

@Component
public class WalletClient {

    private final RestTemplate restTemplate ;

    public WalletClient(RestTemplate restTemplate)
    {
        this.restTemplate = restTemplate ;
    }

    public UUID getWalletIdForUser(String token){
        try{
            HttpHeaders headers = new HttpHeaders();

            headers.set(
                    HttpHeaders.AUTHORIZATION,
                    "Bearer "+token
            );

            HttpEntity<Void> entity = new HttpEntity<>(headers);

            ResponseEntity<WalletResponse> response = restTemplate.exchange(
                    "http://localhost:8083/api/wallets",
                    HttpMethod.GET,
                    entity,
                    WalletResponse.class
            );

            WalletResponse wallet = response.getBody() ;

            if(wallet == null)
            {
                throw new WalletServiceException("Wallet service returned empty response");
            }
            return wallet.id();
        }
        catch(WalletServiceException ex)
        {
            throw ex ;
        }
        catch (Exception ex)
        {
            throw new WalletServiceException("Unable to retrieve the wallet",ex);
        }
    }

    public void withdraw(String token , BigDecimal amount )
    {
        try{
            HttpHeaders headers = new HttpHeaders();
            headers.set(
                    HttpHeaders.AUTHORIZATION,
                    "Bearer "+token
            );

            headers.setContentType(
                    MediaType.APPLICATION_JSON
            );

            WalletMoneyRequest request = new WalletMoneyRequest(amount);
            HttpEntity<WalletMoneyRequest> entity = new HttpEntity<>(request,headers);
            restTemplate.exchange(
                    "http://localhost:8083/api/wallets/withdraw",
                    HttpMethod.POST,
                    entity,
                    Void.class
            );

        }
        catch (HttpStatusCodeException ex)
        {
            String responseBody = ex.getResponseBodyAsString() ;
            try{
                ObjectMapper objectMapper = new ObjectMapper();
                WalletErrorResponse error = objectMapper.readValue(
                        responseBody,
                        WalletErrorResponse.class
                );

                throw new WalletOperationException(
                        error.message(),
                        error.status(),
                        error.code()

                );
            }
            catch (WalletOperationException walletException)
            {
                throw walletException ;
            }
            catch(Exception parseException){
                throw new WalletOperationException(
                        "Wallet service returned an invalid error response",
                        502,
                        "WALLET_ERROR"
                );
            }
        }
    }

    public void transfer(String token , UUID receiverUserId, BigDecimal amount,String reference) {
        try {
            HttpHeaders headers = new HttpHeaders();

            headers.set(
                    HttpHeaders.AUTHORIZATION,
                    "Bearer " + token
            );

            headers.setContentType(
                    MediaType.APPLICATION_JSON
            );

            WalletTransferRequest request = new WalletTransferRequest(
                    receiverUserId,
                    amount,
                    reference
            );

            HttpEntity<WalletTransferRequest> entity =
                    new HttpEntity<>(request, headers);

            restTemplate.exchange(
                    "http://localhost:8083/api/wallets/transfer",
                    HttpMethod.POST,
                    entity,
                    Void.class
            );

        } catch (HttpStatusCodeException ex) {

            String responseBody = ex.getResponseBodyAsString();

            try {
                ObjectMapper objectMapper = new ObjectMapper();

                WalletErrorResponse error = objectMapper.readValue(
                        responseBody,
                        WalletErrorResponse.class
                );

                throw new WalletOperationException(
                        error.message(),
                        error.status(),
                        error.code()
                );

            } catch (WalletOperationException walletException) {
                throw walletException;

            } catch (Exception parseException) {
                throw new WalletOperationException(
                        "Wallet service returned an invalid error response",
                        502,
                        "WALLET_ERROR"
                );
            }


        }
    }


    public record WalletResponse(
            UUID id,
            UUID userId
    ) {
    }

    public record  WalletErrorResponse(
            int status ,
            String code ,
            String  message,
            String timestamp
    ){}
}