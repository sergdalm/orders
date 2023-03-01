package integration;

import lombok.AllArgsConstructor;
import org.example.ApplicationRunner;
import org.example.dto.OrderReadDto;
import org.example.entity.Order;
import org.example.repository.OrderRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AllArgsConstructor
@Transactional
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = ApplicationRunner.class)
public class OrderControllerIT {

    private final MockMvc mockMvc;
    private final OrderRepository orderRepository;

    @BeforeEach
    void setUp() {
        final Order firstOrder = Order.builder()
                .number("2023-02-28-34434")
                .creationDate(LocalDate.of(2023, 2, 28))
                .sum(BigDecimal.valueOf(2500.50))
                .emailSent(false)
                .customerEmail("test@@test-email.com")
                .build();
        final Order secondOrder = Order.builder()
                .number("2023-03-01-264848")
                .creationDate(LocalDate.now())
                .sum(BigDecimal.valueOf(1500))
                .emailSent(true)
                .customerEmail("name@gamil.com")
                .build();

        orderRepository.save(firstOrder);
        orderRepository.save(secondOrder);
    }

    @Test
    void shouldGetAllOrders() throws Exception {
        this.mockMvc.perform(get("/api/v1/orders")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$.[*].number",
                        containsInAnyOrder(getOrderReadDtoWithTestEmail().getNumber(), getOrderReadDtoWithValidEmail().getNumber())));
    }

    @Test
    void shouldGetOrdersWithNotSentNotifications() throws Exception {
        this.mockMvc.perform(get("/api/v1/orders/notSent")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$.[*].number",
                        contains(getOrderReadDtoWithTestEmail().getNumber())));
    }

    @Test
    void shouldGetOrderByOrderNumber() throws Exception {
        this.mockMvc.perform(get("/api/v1/orders/{orderNumber}", getOrderReadDtoWithValidEmail().getNumber())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("number").value(getOrderReadDtoWithValidEmail().getNumber()));
    }

    @Test
    void shouldCreateNewOrder() throws Exception {
        this.mockMvc.perform(post("/api/v1/orders")
                        .param("orderNumber", "2023-03-01-1234")
                        .param("sum", "2500.00")
                        .param("creationDate", "2023-03-01")
                        .param("customerEmail", "customer@gamil.com")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("number").value("2023-03-01-1234"));

        Optional<Order> optionalOrder = orderRepository.findById("2023-03-01-1234");
        assertThat(optionalOrder).isPresent();
        assertEquals(optionalOrder.get().getNumber(), "2023-03-01-1234");
    }

    @Test
    void shouldReturnStatusConflictWhenAttemptingToCreateAlreadyExitingOrder() throws Exception {
        this.mockMvc.perform(post("/api/v1/orders")
                        .param("orderNumber", getOrderReadDtoWithValidEmail().getNumber())
                        .param("sum", "2500.00")
                        .param("creationDate", "2023-03-01")
                        .param("customerEmail", "customer@gamil.com"))
                .andDo(print())
                .andExpect(status().isConflict());

        final List<Order> orders = orderRepository.findAll();
        assertThat(orders).hasSize(2);
    }

    @Test
    void shouldCreateNewOrderAndReturnStatusBadGateway() throws Exception {
        this.mockMvc.perform(post("/api/v1/orders")
                        .param("orderNumber", "2023-03-01-1234")
                        .param("sum", "2500.00")
                        .param("creationDate", "2023-03-01")
                        .param("customerEmail", "customer@test-email.com")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadGateway());

        Optional<Order> optionalOrder = orderRepository.findById("2023-03-01-1234");
        assertThat(optionalOrder).isPresent();
        assertEquals(optionalOrder.get().getNumber(), "2023-03-01-1234");
        assertEquals(optionalOrder.get().getEmailSent(), false);
    }

    @Test
    void shouldUpdateOrder() throws Exception {
        this.mockMvc.perform(put("/api/v1/orders/{orderNumber}", getOrderReadDtoWithTestEmail().getNumber())
                        .param("email", "customer@gamil.com")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("number").value(getOrderReadDtoWithTestEmail().getNumber()))
                .andExpect(jsonPath("emailSent").value(true));
    }

    @Test
    void shouldDeleteOrder() throws Exception {
        this.mockMvc.perform(delete("/api/v1/orders/{orderNumber}", getOrderReadDtoWithTestEmail().getNumber()))
                .andDo(print())
                .andExpect(status().isNoContent());

        Optional<Order> optionalOrder = orderRepository.findById(getOrderReadDtoWithTestEmail().getNumber());
        assertThat(optionalOrder).isNotPresent();
    }

    @AfterEach
    void cleanUp() {
        orderRepository.findById(getOrderReadDtoWithTestEmail().getNumber()).ifPresent(orderRepository::delete);
        orderRepository.findById(getOrderReadDtoWithValidEmail().getNumber()).ifPresent(orderRepository::delete);
    }

    private OrderReadDto getOrderReadDtoWithTestEmail() {
        return OrderReadDto.builder()
                .number("2023-02-28-34434")
                .creationDate(LocalDate.of(2023, 2, 28))
                .sum(BigDecimal.valueOf(2500.50))
                .emailSent(false)
                .customerEmail("test@test-email.com")
                .build();
    }

    private OrderReadDto getOrderReadDtoWithValidEmail() {
        return OrderReadDto.builder()
                .number("2023-03-01-264848")
                .creationDate(LocalDate.now())
                .sum(BigDecimal.valueOf(1500))
                .emailSent(true)
                .customerEmail("name@gamil.com")
                .build();
    }
}
