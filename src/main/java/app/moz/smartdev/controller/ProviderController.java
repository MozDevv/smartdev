package app.moz.smartdev.controller;

import app.moz.smartdev.configs.ResponseBuilder;
import app.moz.smartdev.configs.ResponseWrapper;
import app.moz.smartdev.dtos.AuthProviderDto;
import app.moz.smartdev.service.AuthProviderService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/providers")
@AllArgsConstructor

public class ProviderController {

    private final AuthProviderService providerService;

    @PostMapping()
    public ResponseWrapper<AuthProviderDto> saveProvider(
            @RequestBody AuthProviderDto providerDto,
            @RequestParam(defaultValue = "1") int pageNumber,
            @RequestParam(defaultValue = "10") int pageSize
    ) {

        try {

            AuthProviderDto provider = providerService.create(providerDto);
            return ResponseBuilder.createResponseWrapper(
                    List.of(provider),
                    pageNumber,
                    pageSize,
                    true,
                    "Provider created successfully"
                    , 201
            );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @GetMapping()
    public ResponseWrapper<AuthProviderDto> getAllProviders(
            @RequestParam(defaultValue = "1") int pageNumber,
            @RequestParam(defaultValue = "10") int pageSize
    ) {
        try {
            List<AuthProviderDto> providerDtos = providerService.getProviders();
            int totalCount = providerDtos.size();
            int totalPages = (int) Math.ceil((double) totalCount / pageSize);
            return ResponseBuilder.createResponseWrapper(
                    providerDtos,
                    pageNumber,
                    pageSize,
                    true,
                    "Providers retrieved successfully",
                    200
            );
        } catch (Exception e) {
            return ResponseBuilder.createErrorResponse("Error fetching providers",
                    null, e.getMessage(), 500);
        }
    }


}
