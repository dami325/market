package io.dami.market.domain.payment;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PaymentServiceUnitTest {

  @InjectMocks
  private PaymentService paymentService;
  @Mock
  private PaymentRepository paymentRepository;

}
