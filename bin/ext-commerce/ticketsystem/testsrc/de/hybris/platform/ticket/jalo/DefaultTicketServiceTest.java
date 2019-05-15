/*
 * [y] hybris Platform
 *
 * Copyright (c) 2018 SAP SE or an SAP affiliate company. All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.ticket.jalo;

import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.ticket.enums.CsResolutionType;
import de.hybris.platform.ticket.enums.CsTicketCategory;
import de.hybris.platform.ticket.enums.CsTicketPriority;
import de.hybris.platform.ticket.enums.CsTicketState;
import de.hybris.platform.ticket.events.model.CsTicketEventModel;
import de.hybris.platform.ticket.model.CsAgentGroupModel;
import de.hybris.platform.ticket.model.CsTicketModel;
import de.hybris.platform.ticket.service.TicketService;
import de.hybris.platform.ticket.service.impl.DefaultTicketBusinessService;
import de.hybris.platform.ticket.strategies.TicketEventEmailStrategy;

import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


/**
 * @author chris
 * 
 */
public class DefaultTicketServiceTest extends AbstractTicketsystemTest
{
	@Resource
	private TicketService ticketService;
	@Resource
	private UserService userService;
	@Resource
	private TypeService typeService;
	@Resource
	private FlexibleSearchService flexibleSearchService;

	private BaseStoreModel testStore1;
	private BaseStoreModel testStore2;


	private MockTicketEventEmailStrategy emailService = null;

	private TicketEventEmailStrategy originalMailService = null;

	private MockTicketEventEmailStrategy getEmailService()
	{
		if (emailService == null)
		{
			emailService = new MockTicketEventEmailStrategy();
			emailService.setModelService(modelService);
		}
		return emailService;
	}

	@Override
	@Before
	public void setUp() throws Exception
	{
		super.setUp();

		originalMailService = ((DefaultTicketBusinessService) ticketBusinessService).getTicketEventEmailStrategy();
		((DefaultTicketBusinessService) ticketBusinessService).setTicketEventEmailStrategy(getEmailService());

		testStore1 = (BaseStoreModel) flexibleSearchService
				.search("SELECT {pk} from {BaseStore} where {uid}=?uid", Collections.singletonMap("uid", "csTicketTestStore"))
				.getResult().get(0);
		testStore2 = (BaseStoreModel) flexibleSearchService
				.search("SELECT {pk} from {BaseStore} where {uid}=?uid", Collections.singletonMap("uid", "csOtherTicketTestStore"))
				.getResult().get(0);
	}

	@After
	public void tearDown()
	{
		// implement here code executed after each test
		getEmailService().reset();
		((DefaultTicketBusinessService) ticketBusinessService).setTicketEventEmailStrategy(originalMailService);
	}

	/**
	 * This is a sample test method.
	 */
	@Test
	public void testDefaultTicketService()
	{
		assertNotNull("Ticket Service Not Found", ticketService);
	}

	@Test
	public void testGetTicketForTicketEventByID()
	{
		assertNull("Ticket Found with no ticketId", ticketService.getTicketForTicketId(""));
		assertNull("Ticket Found with no ticketId", ticketService.getTicketForTicketId(null));
		assertNotNull("No Ticket Found with supplied ticketid", ticketService.getTicketForTicketId("test-ticket-1"));
		assertEquals("No Ticket Found with supplied ticketid", "test-ticket-1", ticketService.getTicketForTicketId("test-ticket-1")
				.getTicketID());
	}

	@Test
	public void testGetTicketForTicketEventsByCustomer()
	{
		assertEquals("getTicketsByCustomer(null) returned <> 0", 0, ticketService.getTicketsForCustomer(null).size());

		final UserModel ariel = userService.getUser("ariel");
		final UserModel ppetersonson = userService.getUser("ppetersonson");

		assertEquals("Unexpected number of Tickets for customer 'ariel'", 3, ticketService.getTicketsForCustomer(ariel).size());
		assertEquals("Unexpected number of Tickets for customer 'ppetersonson'", 1, ticketService
				.getTicketsForCustomer(ppetersonson).size());
	}

	@Test
	public void testGetTicketForTicketEventsByOrder()
	{
		assertEquals("getTicketsByOrder(null) returned <> 0", 0, ticketService.getTicketsForOrder(null).size());

		final String orderTypeCode = typeService.getComposedType(OrderModel.class).getCode();

		final String query = "SELECT DISTINCT {" + OrderModel.PK + "}, {" + OrderModel.CODE + "} FROM { " + orderTypeCode + "}"
				+ " WHERE {" + OrderModel.CODE + "} LIKE ?orderId AND {" + OrderModel.ORIGINALVERSION + "} IS NULL ";

		final SearchResult<OrderModel> ticketTestOrder1 = flexibleSearchService.search(query,
				Collections.singletonMap("orderId", "ticketTestOrder1"));

		assertEquals("Unexpected number of Tickets for Order O-K2006-C0001-001", 1,
				ticketService.getTicketsForOrder(ticketTestOrder1.getResult().get(0)).size());

		final SearchResult<OrderModel> ticketTestOrder2 = flexibleSearchService.search(query,
				Collections.singletonMap("orderId", "ticketTestOrder2"));

		assertEquals("Unexpected number of Tickets for Order O-K2006-C0001-002", 2,
				ticketService.getTicketsForOrder(ticketTestOrder2.getResult().get(0)).size());
	}

	@Test
	public void testGetTicketForTicketEventsByAgent()
	{
		assertTrue("getTicketsByAgent(null) shouldn't fail", ticketService.getTicketsForAgent(null).size() >= 0);

		assertEquals("Unexpected number of Tickets for agent 'admin'", 6, ticketService.getTicketsForAgent(adminUser).size());
	}

	@Test
	public void testGetTicketForTicketEventsByAgentGroup()
	{
		assertEquals("getTicketsByAgentGroup(null) returned <> 1", 1, ticketService.getTicketsForAgentGroup(null).size());

		final CsAgentGroupModel testTicketGroup = (CsAgentGroupModel) userService.getUserGroup("testTicketGroup1");

		assertEquals("Unexpected number of Tickets for agentGroup 'testTicketGroup1'", 6,
				ticketService.getTicketsForAgentGroup(testTicketGroup).size());
	}

	@Test
	public void testGetTicketForTicketEventsByState()
	{
		assertEquals("getTicketsByState(null) returned <> 0", 0, ticketService.getTicketsForState().size());
		assertEquals("getTicketsByState(new CsTicketState[] {}) returned <> 0", 0, ticketService.getTicketsForState().size());
		assertEquals("Unexpected number of Tickets in State 'New'", 5, ticketService.getTicketsForState(CsTicketState.NEW).size());
		assertEquals("Unexpected number of Tickets in State 'Open'", 1, ticketService.getTicketsForState(CsTicketState.OPEN).size());
		assertEquals("Unexpected number of Tickets in State 'Closed'", 1, ticketService.getTicketsForState(CsTicketState.CLOSED)
				.size());
		assertEquals("Unexpected number of Tickets in States 'New, Open, Closed'", 7,
				ticketService.getTicketsForState(CsTicketState.NEW, CsTicketState.OPEN, CsTicketState.CLOSED).size());
	}

	@Test
	public void testGetTicketForTicketEventsByPriority()
	{
		assertEquals("getTicketsByPriority(null) returned <> 0", 0, ticketService.getTicketsForPriority().size());
		assertEquals("getTicketsByPriority(new CsTicketPriority[] {}) returned <> 0", 0, ticketService.getTicketsForPriority()
				.size());
		assertEquals("Unexpected number of Tickets with Priorities 'High, Medium, Low'", 7,
				ticketService.getTicketsForPriority(CsTicketPriority.HIGH, CsTicketPriority.MEDIUM, CsTicketPriority.LOW).size());
		assertEquals("Unexpected number of Tickets with Priority 'High'", 4,
				ticketService.getTicketsForPriority(CsTicketPriority.HIGH).size());
		assertEquals("Unexpected number of Tickets with Priority 'Medium'", 1,
				ticketService.getTicketsForPriority(CsTicketPriority.MEDIUM).size());
		assertEquals("Unexpected number of Tickets with Priority 'Low'", 2, ticketService
				.getTicketsForPriority(CsTicketPriority.LOW).size());
	}

	@Test
	public void testGetTicketForTicketEventsByCategory()
	{
		assertEquals("getTicketsByCategory(null) returned <> 0", 0, ticketService.getTicketsForCategory().size());
		assertEquals("getTicketsByCategory(new CsTicketCategory[] {}) returned <> 0", 0, ticketService.getTicketsForCategory()
				.size());
		assertEquals(
				"Unexpected number of Tickets with Categories 'Complaint, Incident, Note, Problem'",
				7,
				ticketService.getTicketsForCategory(CsTicketCategory.COMPLAINT, CsTicketCategory.INCIDENT, CsTicketCategory.NOTE,
						CsTicketCategory.PROBLEM).size());
		assertEquals("Unexpected number of Tickets with Category 'Complaint'", 0,
				ticketService.getTicketsForCategory(CsTicketCategory.COMPLAINT).size());
		assertEquals("Unexpected number of Tickets with Category 'Incident'", 1,
				ticketService.getTicketsForCategory(CsTicketCategory.INCIDENT).size());
		assertEquals("Unexpected number of Tickets with Category 'Note'", 4,
				ticketService.getTicketsForCategory(CsTicketCategory.NOTE).size());
		assertEquals("Unexpected number of Tickets with Category 'Problem'", 2,
				ticketService.getTicketsForCategory(CsTicketCategory.PROBLEM).size());
	}

	@Test
	public void testGetTicketForTicketEventsByResolutionType()
	{
		assertEquals("getTicketsByResolutionType(null) returned <> 0", 0, ticketService.getTicketsForResolutionType().size());
		assertEquals("getTicketsByResolutionType(new CsResolutionType[] {}) returned <> 0", 0, ticketService
				.getTicketsForResolutionType().size());
		assertEquals("Unexpected number of Tickets with ResolutionTypes 'Closed, ClosedDuplicate'", 1, ticketService
				.getTicketsForResolutionType(CsResolutionType.CLOSED, CsResolutionType.CLOSEDDUPLICATE).size());
		assertEquals("Unexpected number of Tickets with ResolutionType 'Closed'", 1,
				ticketService.getTicketsForResolutionType(CsResolutionType.CLOSED).size());
		assertEquals("Unexpected number of Tickets with ResolutionType 'losedDuplicate'", 0, ticketService
				.getTicketsForResolutionType(CsResolutionType.CLOSEDDUPLICATE).size());
	}

	@Test
	public void testGetAgents()
	{
		//assertEquals("getAgents() returned <> 0", 4, ticketService.getAgents().size());

		assertEquals("getAgents(m_testStore1) returned <> 0", 3, ticketService.getAgentsForBaseStore(testStore1).size());

		assertEquals("getAgents(m_testStore2) returned <> 0", 1, ticketService.getAgentsForBaseStore(testStore2).size());
	}

	@Test
	public void testGetAgentGroups()
	{
		assertEquals("getAgentGroups() returned <> 0", 3, ticketService.getAgentGroups().size());

		assertEquals("getAgentGroups(m_testStore1) returned <> 0", 2, ticketService.getAgentGroupsForBaseStore(testStore1).size());

		assertEquals("getAgentGroups(m_testStore2) returned <> 0", 1, ticketService.getAgentGroupsForBaseStore(testStore2).size());
	}

	@Test
	public void shouldReturnTicketEventsForTicket()
	{
		// given
		final List<CsTicketModel> ticketsByAgent = ticketService.getTicketsForAgent(null);
		final CsTicketModel csTicketModel = ticketsByAgent.get(0);

		// when
		final List<CsTicketEventModel> ticketEventsForTicket = ticketService.getEventsForTicket(csTicketModel);

		// then
		assertThat(ticketEventsForTicket).hasSize(1);
	}

}
