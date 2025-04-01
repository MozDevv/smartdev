package app.moz.smartdev.controller;

import app.moz.smartdev.configs.ResponseBuilder;
import app.moz.smartdev.configs.ResponseWrapper;
import app.moz.smartdev.dtos.ProviderDto;
import app.moz.smartdev.service.ProviderService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/providers")
@AllArgsConstructor

public class ProviderController {

    private final ProviderService providerService;

    @PostMapping()
    public ResponseWrapper<ProviderDto> saveProvider(
            @RequestBody ProviderDto providerDto,
            @RequestParam(defaultValue = "1") int pageNumber,
            @RequestParam(defaultValue = "10") int pageSize
    ) {

        try {

            ProviderDto provider = providerService.create(providerDto);
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


}
