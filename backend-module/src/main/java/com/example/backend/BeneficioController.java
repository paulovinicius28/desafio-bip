package com.example.backend;

import com.example.backend.dto.BeneficioDTO;
import com.example.backend.mapper.BeneficioMapper;
import com.example.common.entity.Beneficio;
import com.example.backend.service.BeneficioEjbService;
import com.example.backend.service.IBeneficioEjbService;
import com.example.common.response.TransferResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.*;

@RestController
@RequestMapping("/api/v1/beneficios")
@Tag(name = "Benefícios", description = "APIs para gerenciar benefícios")
public class BeneficioController {

    @Autowired
    private BeneficioEjbService beneficioService;

    @Autowired
    private BeneficioMapper beneficioMapper;

    @Operation(
            summary = "Listar todos os benefícios",
            description = "Retorna uma lista com todos os benefícios registrados no sistema. " +
                    "Se nenhum benefício estiver registrado, retorna uma lista vazia."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista de benefícios retornada com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = BeneficioDTO.class),
                            examples = @ExampleObject(
                                    value = "[{\"id\": 1, \"nome\": \"Auxílio Alimentação\", \"valor\": 1500.00}, " +
                                            "{\"id\": 2, \"nome\": \"Auxílio Transporte\", \"valor\": 500.00}]"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Erro interno do servidor ao listar benefícios",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = TransferResponse.class)
                    )
            )
    })
    @GetMapping
    public ResponseEntity<List<BeneficioDTO>> list() {
        List<Beneficio> beneficios = beneficioService.findAll();
        List<BeneficioDTO> beneficioDTOs = beneficioMapper.toDTOList(beneficios);
        return ResponseEntity.ok(beneficioDTOs);
    }

    @Operation(
            summary = "Buscar benefício por ID",
            description = "Recupera um benefício específico do sistema pelo seu identificador único. " +
                    "Se o benefício não existir, retorna uma resposta de erro 404."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Benefício encontrado com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = BeneficioDTO.class),
                            examples = @ExampleObject(
                                    value = "{\"id\": 1, \"nome\": \"Auxílio Alimentação\", \"valor\": 1500.00}"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Benefício não encontrado com o ID fornecido",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = TransferResponse.class),
                            examples = @ExampleObject(
                                    value = "{\"message\": \"Benefício não encontrado\", \"httpStatus\": 404}"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Erro interno do servidor ao processar a requisição",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = TransferResponse.class)
                    )
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<BeneficioDTO> findById(
            @Parameter(
                    name = "id",
                    description = "Identificador único do benefício a ser buscado",
                    example = "1",
                    required = true
            )
            @PathVariable Long id) {
        Beneficio beneficio = beneficioService.findById(id).orElse(new Beneficio());
        BeneficioDTO beneficioDTO = beneficioMapper.toDTO(beneficio);
        return ResponseEntity.ok(beneficioDTO);
    }

    @Operation(
            summary = "Criar novo benefício",
            description = "Cria um novo benefício no sistema com nome e valor inicial. " +
                    "O ID é gerado automaticamente pelo servidor."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Benefício criado com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = BeneficioDTO.class),
                            examples = @ExampleObject(
                                    value = "{\"id\": 1, \"nome\": \"Auxílio Alimentação\", \"valor\": 1500.00}"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Dados inválidos: nome vazio ou valor negativo",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = TransferResponse.class),
                            examples = @ExampleObject(
                                    value = "{\"message\": \"Nome do benefício é obrigatório\", \"httpStatus\": 400}"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Erro interno do servidor ao criar o benefício",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = TransferResponse.class)
                    )
            )
    })
    @PostMapping
    public ResponseEntity<BeneficioDTO> create( @RequestBody @Valid BeneficioDTO beneficio) {
        Beneficio beneficioEntity = beneficioMapper.toEntity(beneficio);
        Beneficio beneficioCreated = beneficioService.save(beneficioEntity);
        BeneficioDTO beneficioDTO = beneficioMapper.toDTO(beneficioCreated);
        return ResponseEntity.status(HttpStatus.CREATED).body(beneficioDTO);
    }

    @Operation(
            summary = "Atualizar benefício",
            description = "Atualiza os dados de um benefício existente. " +
                    "Pode atualizar o nome e/ou o valor do benefício."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Benefício atualizado com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = BeneficioDTO.class),
                            examples = @ExampleObject(
                                    value = "{\"id\": 1, \"nome\": \"Auxílio Transporte\", \"valor\": 2000.00}"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Dados inválidos: nome vazio ou valor negativo",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = TransferResponse.class),
                            examples = @ExampleObject(
                                    value = "{\"message\": \"Dados inválidos: Nome do benefício é obrigatório\", \"httpStatus\": 400}"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Benefício com o ID fornecido não encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = TransferResponse.class),
                            examples = @ExampleObject(
                                    value = "{\"message\": \"Benefício não encontrado\", \"httpStatus\": 404}"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Erro interno do servidor ao atualizar o benefício",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = TransferResponse.class)
                    )
            )
    })
    @PutMapping("/{id}")
    public ResponseEntity<BeneficioDTO> update(
            @Parameter(
                    name = "id",
                    description = "Identificador único do benefício a ser atualizado",
                    example = "1",
                    required = true
            )
            @PathVariable Long id,
            @RequestBody @Valid BeneficioDTO beneficio) {
        beneficio.setId(id);
        Beneficio beneficioEntity = beneficioMapper.toEntity(beneficio);
        Beneficio beneficioUpdated = beneficioService.update(beneficioEntity);
        BeneficioDTO beneficioDTO = beneficioMapper.toDTO(beneficioUpdated);
        return ResponseEntity.ok(beneficioDTO);
    }

    @Operation(
            summary = "Transferir valor entre benefícios",
            description = "Realiza uma transferência de valor entre dois benefícios. " +
                    "Valida se o benefício de origem possui saldo suficiente e se ambos os benefícios existem. " +
                    "A transferência é realizada com lock pessimista para garantir consistência."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Transferência realizada com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = TransferResponse.class),
                            examples = @ExampleObject(
                                    value = "{\"message\": \"Transferência realizada com sucesso\", \"httpStatus\": 200}"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Erro de validação: saldo insuficiente, benefício não encontrado ou valor inválido",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = TransferResponse.class),
                            examples = @ExampleObject(
                                    value = "{\"message\": \"Saldo insuficiente\", \"httpStatus\": 400}"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Erro interno do servidor ao processar a transferência",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = TransferResponse.class)
                    )
            )
    })
    @PutMapping("/transfer")
    public ResponseEntity<TransferResponse> transfer(
            @Parameter(
                    name = "idBeneficioFrom",
                    description = "ID do benefício de origem (conta debitada). Deve existir e ter saldo suficiente",
                    example = "1",
                    required = true
            )
            @RequestParam Long idBeneficioFrom,
            
            @Parameter(
                    name = "idBeneficioTo",
                    description = "ID do benefício de destino (conta creditada). Deve existir",
                    example = "2",
                    required = true
            )
            @RequestParam Long idBeneficioTo,
            
            @Parameter(
                    name = "amountToTransfer",
                    description = "Valor a ser transferido. Deve ser maior que zero",
                    example = "1000.00",
                    required = true
            )
            @RequestParam BigDecimal amountToTransfer) {
        beneficioService.transfer(idBeneficioFrom, idBeneficioTo, amountToTransfer);
        return ResponseEntity.ok(new TransferResponse("Transferência realizada com sucesso", HttpStatus.OK.value()));
    }

    @Operation(
            summary = "Deletar benefício",
            description = "Remove um benefício do sistema permanentemente pelo seu ID. " +
                    "Após a exclusão, o benefício não poderá ser recuperado. " +
                    "Se o benefício não existir, retorna erro 404."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Benefício deletado com sucesso. Sem conteúdo na resposta"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Benefício não encontrado com o ID fornecido",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = TransferResponse.class),
                            examples = @ExampleObject(
                                    value = "{\"message\": \"Benefício não encontrado\", \"httpStatus\": 404}"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Erro interno do servidor ao deletar o benefício",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = TransferResponse.class)
                    )
            )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(
                    name = "id",
                    description = "Identificador único do benefício a ser deletado",
                    example = "1",
                    required = true
            )
            @PathVariable Long id) {
        beneficioService.delete(id);
        return ResponseEntity.noContent().build();
    }
}