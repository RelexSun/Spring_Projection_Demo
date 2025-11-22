package com.kshrd.springprojection.controller;

import com.kshrd.springprojection.dto.baseResponse.APIResponse;
import com.kshrd.springprojection.dto.request.AccountRequest;
import com.kshrd.springprojection.dto.response.AccountResponse;
import com.kshrd.springprojection.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.kshrd.springprojection.utils.ResponseUtil.buildResponse;

@RestController
@RequestMapping("/api/v1/accounts")
@RequiredArgsConstructor
@Tag(
        name = "Account",
        description = "Endpoints for managing accounts, including CRUD operations."
)
public class AccountController {

    private final AccountService accountService;

    @Operation(
            summary = "Get all accounts",
            description = "Fetches a list of all accounts.",
            tags = {"Account"}
    )
    @GetMapping
    public ResponseEntity<APIResponse<List<AccountResponse>>> getAll() {
        return buildResponse(
                "Fetched all accounts",
                accountService.getAll(),
                HttpStatus.OK
        );
    }

    @Operation(
            summary = "Get account by ID",
            description = "Retrieves a single account by its ID.",
            tags = {"Account"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Account retrieved successfully",
                    content = @Content(schema = @Schema(implementation = AccountResponse.class))),
            @ApiResponse(responseCode = "404", description = "Account not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<APIResponse<AccountResponse>> getById(@PathVariable Long id) {
        return buildResponse(
                "Account found",
                accountService.getById(id),
                HttpStatus.OK
        );
    }

    @Operation(
            summary = "Create a new account",
            description = "Creates a new account with the provided information.",
            tags = {"Account"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Account created successfully",
                    content = @Content(schema = @Schema(implementation = AccountResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request payload")
    })
    @PostMapping
    public ResponseEntity<APIResponse<AccountResponse>> create(@RequestBody AccountRequest req) {
        return buildResponse(
                "Account created",
                accountService.create(req),
                HttpStatus.CREATED
        );
    }

    @Operation(
            summary = "Update account by ID",
            description = "Updates an existing account identified by its ID.",
            tags = {"Account"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Account updated successfully",
                    content = @Content(schema = @Schema(implementation = AccountResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request payload"),
            @ApiResponse(responseCode = "404", description = "Account not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<APIResponse<AccountResponse>> update(
            @PathVariable Long id,
            @RequestBody AccountRequest req
    ) {
        return buildResponse(
                "Account updated",
                accountService.update(id, req),
                HttpStatus.OK
        );
    }

    @Operation(
            summary = "Delete account by ID",
            description = "Deletes an account permanently by its ID.",
            tags = {"Account"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Account deleted successfully",
                    content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "404", description = "Account not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<APIResponse<String>> delete(@PathVariable Long id) {
        accountService.delete(id);
        return buildResponse(
                "Account deleted",
                "Deleted account ID = " + id,
                HttpStatus.OK
        );
    }
}
