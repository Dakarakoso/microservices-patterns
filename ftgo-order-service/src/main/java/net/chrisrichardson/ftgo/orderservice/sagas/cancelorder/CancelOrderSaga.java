package net.chrisrichardson.ftgo.orderservice.sagas.cancelorder;

import io.eventuate.tram.commands.consumer.CommandWithDestination;
import io.eventuate.tram.sagas.orchestration.SagaDefinition;
import io.eventuate.tram.sagas.simpledsl.SimpleSaga;
import net.chrisrichardson.ftgo.orderservice.api.OrderServiceChannels;
import net.chrisrichardson.ftgo.orderservice.sagaparticipants.BeginCancelCommand;
import net.chrisrichardson.ftgo.orderservice.sagaparticipants.ConfirmCancelOrderCommand;
import net.chrisrichardson.ftgo.orderservice.sagaparticipants.UndoBeginCancelCommand;

import org.springframework.util.Assert;

import com.ftgo.accountservice.api.AccountingServiceChannels;
import com.ftgo.accountservice.api.command.ReverseAuthorizationCommand;
import com.ftgo.kitchenservice.api.KitchenServiceChannels;
import com.ftgo.kitchenservice.api.command.BeginCancelTicketCommand;
import com.ftgo.kitchenservice.api.command.ConfirmCancelTicketCommand;
import com.ftgo.kitchenservice.api.command.UndoBeginCancelTicketCommand;

import javax.annotation.PostConstruct;

import static io.eventuate.tram.commands.consumer.CommandWithDestinationBuilder.send;

public class CancelOrderSaga implements SimpleSaga<CancelOrderSagaData> {
	private SagaDefinition<CancelOrderSagaData> sagaDefinition;

	@PostConstruct
	public void initializeSagaDefinition() {
		sagaDefinition = step().invokeParticipant(this::beginCancel)
				.withCompensation(this::undoBeginCancel).step()
				.invokeParticipant(this::beginCancelTicket)
				.withCompensation(this::undoBeginCancelTicket).step()
				.invokeParticipant(this::reverseAuthorization).step()
				.invokeParticipant(this::confirmTicketCancel).step()
				.invokeParticipant(this::confirmOrderCancel).build();

	}

	private CommandWithDestination confirmOrderCancel(CancelOrderSagaData data) {
		return send(new ConfirmCancelOrderCommand(data.getOrderId())).to(
				OrderServiceChannels.orderServiceChannel).build();
	}

	private CommandWithDestination confirmTicketCancel(CancelOrderSagaData data) {
		return send(new ConfirmCancelTicketCommand(data.getRestaurantId(), data
						.getOrderId())).to(KitchenServiceChannels.kitchenServiceChannel).build();
	}

	private CommandWithDestination reverseAuthorization(CancelOrderSagaData data) {
		return send(new ReverseAuthorizationCommand(data.getConsumerId(), data
						.getOrderId(), data.getOrderTotal())).to(AccountingServiceChannels.accountingServiceChannel).build();
	}

	private CommandWithDestination undoBeginCancelTicket(CancelOrderSagaData data) {
		return send(new UndoBeginCancelTicketCommand(data.getRestaurantId(), data
						.getOrderId())).to(KitchenServiceChannels.kitchenServiceChannel).build();
	}

	private CommandWithDestination beginCancelTicket(CancelOrderSagaData data) {
		return send(new BeginCancelTicketCommand(data.getRestaurantId(),
						(long) data.getOrderId())).to(KitchenServiceChannels.kitchenServiceChannel).build();
	}

	private CommandWithDestination undoBeginCancel(CancelOrderSagaData data) {
		return send(new UndoBeginCancelCommand(data.getOrderId())).to(
				OrderServiceChannels.orderServiceChannel).build();
	}

	private CommandWithDestination beginCancel(CancelOrderSagaData data) {
		return send(new BeginCancelCommand(data.getOrderId())).to(
				OrderServiceChannels.orderServiceChannel).build();
	}

	@Override
	public SagaDefinition<CancelOrderSagaData> getSagaDefinition() {
		Assert.notNull(sagaDefinition);
		return sagaDefinition;
	}
}
