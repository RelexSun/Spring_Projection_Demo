package com.kshrd.springprojection.controller;

import com.kshrd.springprojection.dto.baseResponse.APIResponse;
import com.kshrd.springprojection.dto.baseResponse.PagedResponse;
import com.kshrd.springprojection.dto.projection.TransactionSummary;
import com.kshrd.springprojection.dto.projection.TransactionWithAccount;
import com.kshrd.springprojection.dto.request.TransactionRequest;
import com.kshrd.springprojection.dto.response.TransactionResponse;
import com.kshrd.springprojection.enumeration.ProjectionType;
import com.kshrd.springprojection.enumeration.TransactionProperty;
import com.kshrd.springprojection.enumeration.TransactionType;
import com.kshrd.springprojection.exception.BadRequestException;
import com.kshrd.springprojection.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

import static com.kshrd.springprojection.utils.ResponseUtil.buildResponse;

@RequestMapping("/api/v1/transactions")
@RestController
@RequiredArgsConstructor
@Tag(
    name = "Transaction",
    description = "Endpoints for managing transactions, including CRUD operations and projection-based listings."
)
public class TransactionController {
    private final TransactionService transactionService;

    @Operation(
            summary = "Get transactions by type (nested projection)",
            description = "Returns transactions with nested account information using projection",
            tags = {"Transaction"}
    )
    @GetMapping("/type")
    public ResponseEntity<APIResponse<List<TransactionWithAccount>>> getByType(
            @RequestParam TransactionType type
    ) {
        return buildResponse(
                "Fetched transactions by type",
                transactionService.getByType(type.getFieldName()),
                HttpStatus.OK
        );
    }

    @Operation(
            summary = "Dynamic projection endpoint",
            description = "Allows clients to choose which projection to return dynamically",
            tags = {"Transaction"}
    )
    @GetMapping("/projection")
    public ResponseEntity<APIResponse<List<?>>> getDynamicProjection(
            @RequestParam BigDecimal amount,
            @RequestParam ProjectionType type
    ) {
        Class<?> projection;

        switch (type) {
            case SUMMARY -> projection = TransactionSummary.class;
            case WITH_ACCOUNT -> projection = TransactionWithAccount.class;
            default -> throw new BadRequestException("Invalid projection type");
        }

        List<?> result = transactionService.getByAmountGreaterThan(amount, projection);

        return buildResponse("Dynamic projection fetched", result, HttpStatus.OK);
    }

    @Operation(
            summary = "Get paginated transactions",
            description = """
                Returns transactions using pagination, sorting, and projection.
                Uses 1-based page indexing from the client side and 
                safely converts to 0-based index for Spring Data.
                """,
            tags = {"Transaction"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Paginated list retrieved",
                    content = @Content(schema = @Schema(implementation = PagedResponse.class)))
    })
    @GetMapping("/paged")
    public ResponseEntity<APIResponse<PagedResponse<Page<TransactionSummary>>>> getPaged(
            @Parameter(description = "Page number starting from 1", example = "1")
            @RequestParam(defaultValue = "1") Integer page,

            @Parameter(description = "Page size", example = "10")
            @RequestParam(defaultValue = "10") Integer size,

            @Parameter(description = "Sort direction", example = "DESC")
            @RequestParam(defaultValue = "DESC") Sort.Direction direction,

            @Parameter(description = "Sort field", example = "timestamp")
            @RequestParam(defaultValue = "timestamp") TransactionProperty sortBy
    ) {

        return buildResponse(
                "Fetched all transactions",
                transactionService.getPaged(page, size, direction, sortBy),
                HttpStatus.OK
        );
    }


    @Operation(
            summary = "Get all transactions (Projection)",
            description = """
                Returns a list of all transactions using projection-based response. 
                This endpoint is optimized for read-intensive views and avoids fetching unnecessary fields.
                """,
            tags = {"Transaction"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Transactions retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TransactionSummary.class)))
    })
    @GetMapping
    public ResponseEntity<APIResponse<List<TransactionSummary>>> getAll() {
        return buildResponse(
                "Fetched all transactions",
                transactionService.getAll(),
                HttpStatus.OK
        );
    }

    @Operation(
            summary = "Create a new transaction",
            description = """
                Creates a new transaction using the provided request payload.
                Requires a valid account ID. Returns a detailed transaction response.
                """,
            tags = {"Transaction"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Transaction created successfully",
                    content = @Content(schema = @Schema(implementation = TransactionResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request payload"),
            @ApiResponse(responseCode = "404", description = "Account not found")
    })
    @PostMapping
    public ResponseEntity<APIResponse<TransactionResponse>> create(
            @RequestBody
            @Valid
            @Parameter(description = "Payload containing transaction information", required = true)
            TransactionRequest req
    ) {
        return buildResponse(
                "Transaction created",
                transactionService.create(req),
                HttpStatus.CREATED
        );
    }

    @Operation(
            summary = "Get transaction by ID",
            description = """
                Retrieves a single transaction by its ID.
                Returns a detailed transaction response including account information.
                """,
            tags = {"Transaction"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Transaction retrieved successfully",
                    content = @Content(schema = @Schema(implementation = TransactionResponse.class))),
            @ApiResponse(responseCode = "404", description = "Transaction not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<APIResponse<TransactionResponse>> getById(
            @Parameter(description = "Transaction ID", required = true)
            @PathVariable Long id
    ) {
        return buildResponse(
                "Transaction found",
                transactionService.getById(id),
                HttpStatus.OK
        );
    }

    @Operation(
            summary = "Update transaction by ID",
            description = """
                Updates an existing transaction based on the provided request payload.
                The transaction is identified by its ID.
                """,
            tags = {"Transaction"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Transaction updated successfully",
                    content = @Content(schema = @Schema(implementation = TransactionResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request payload"),
            @ApiResponse(responseCode = "404", description = "Transaction or account not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<APIResponse<TransactionResponse>> update(
            @Parameter(description = "Transaction ID", required = true)
            @PathVariable Long id,

            @Parameter(description = "Updated transaction payload", required = true)
            @RequestBody @Valid TransactionRequest req
    ) {
        return buildResponse(
                "Transaction updated",
                transactionService.update(id, req),
                HttpStatus.OK
        );
    }

    @Operation(
            summary = "Delete transaction by ID",
            description = """
                Deletes a transaction permanently using its ID.
                Returns a confirmation message upon successful deletion.
                """,
            tags = {"Transaction"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Transaction deleted successfully",
                    content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "404", description = "Transaction not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<APIResponse<String>> delete(
            @Parameter(description = "Transaction ID", required = true)
            @PathVariable Long id
    ) {
        transactionService.delete(id);
        return buildResponse(
                "Transaction deleted",
                "Deleted transaction ID = " + id,
                HttpStatus.OK
        );
    }

}
