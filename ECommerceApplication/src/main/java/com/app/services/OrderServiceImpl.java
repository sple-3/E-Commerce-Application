package com.app.services;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.app.entites.Cart;
import com.app.entites.CartItem;
import com.app.entites.Membership;
import com.app.entites.Order;
import com.app.entites.OrderItem;
import com.app.entites.Payment;
import com.app.entites.Product;
import com.app.exceptions.APIException;
import com.app.exceptions.ResourceNotFoundException;
import com.app.payloads.OrderDTO;
import com.app.payloads.OrderItemDTO;
import com.app.payloads.OrderResponse;
import com.app.repositories.CartItemRepo;
import com.app.repositories.CartRepo;
import com.app.repositories.MembershipRepo;
import com.app.repositories.OrderItemRepo;
import com.app.repositories.OrderRepo;
import com.app.repositories.PaymentRepo;
import com.app.repositories.UserRepo;

import jakarta.transaction.Transactional;

@Transactional
@Service
public class OrderServiceImpl implements OrderService {

	@Autowired
	public UserRepo userRepo;

	@Autowired
	public CartRepo cartRepo;

	@Autowired
	public OrderRepo orderRepo;

	@Autowired
	private PaymentRepo paymentRepo;

	@Autowired
	public OrderItemRepo orderItemRepo;

	@Autowired
	public CartItemRepo cartItemRepo;

	@Autowired
	private MembershipRepo membershipRepo;

	@Autowired
	public UserService userService;

	@Autowired
	public CartService cartService;

	@Autowired
	public ModelMapper modelMapper;

	@Override
	public OrderDTO placeOrder(String email, Long cartId, String paymentMethod, String membershipCode,
			String codAddress) {

		Cart cart = cartRepo.findCartByEmailAndCartId(email, cartId);

		if (cart == null) {
			throw new ResourceNotFoundException("Cart", "cartId", cartId);
		}

		List<CartItem> cartItems = cart.getCartItems();

		if (cartItems.size() == 0) {
			throw new APIException("Cart is empty");
		}

		Membership membership = null;
		String sanitizedMembershipCode = null;
		double membershipDiscountPercent = 0;

		if (membershipCode != null && !membershipCode.trim().isEmpty()) {
			sanitizedMembershipCode = membershipCode.trim();
			membership = membershipRepo.findByCodeAndActiveTrue(sanitizedMembershipCode)
					.orElseThrow(() -> new APIException("Invalid or inactive membership code"));
			membershipDiscountPercent = membership.getDiscountPercent();
		}

		String normalizedPaymentMethod = paymentMethod == null ? "" : paymentMethod.trim();
		String sanitizedCodAddress = (codAddress == null || codAddress.trim().isEmpty()) ? null : codAddress.trim();

		if ("CASH_ON_DELIVERY".equalsIgnoreCase(normalizedPaymentMethod) && sanitizedCodAddress == null) {
			throw new APIException("codAddress is required for CASH_ON_DELIVERY payment");
		}

		if (!"CASH_ON_DELIVERY".equalsIgnoreCase(normalizedPaymentMethod)) {
			sanitizedCodAddress = null;
		}

		final double appliedMembershipDiscountPercent = membershipDiscountPercent;

		Order order = new Order();

		order.setEmail(email);
		order.setOrderDate(LocalDate.now());
		order.setMembershipCode(sanitizedMembershipCode);
		order.setCodAddress(sanitizedCodAddress);

		double totalAmount = cart.getTotalPrice();
		if (membership != null) {
			totalAmount = cartItems.stream().mapToDouble(item -> {
				double basePrice = item.getProduct().getPrice();
				double discountedPrice = basePrice - ((appliedMembershipDiscountPercent * 0.01) * basePrice);
				return discountedPrice * item.getQuantity();
			}).sum();
		}

		order.setTotalAmount(totalAmount);
		order.setOrderStatus("Order Accepted !");

		Payment payment = new Payment();
		payment.setOrder(order);
		payment.setPaymentMethod(normalizedPaymentMethod);

		payment = paymentRepo.save(payment);

		order.setPayment(payment);

		Order savedOrder = orderRepo.save(order);

		List<OrderItem> orderItems = new ArrayList<>();

		for (CartItem cartItem : cartItems) {
			OrderItem orderItem = new OrderItem();
			Product product = cartItem.getProduct();
			double orderedProductPrice = cartItem.getProductPrice();
			double discountPercent = cartItem.getDiscount();

			if (membership != null) {
				orderedProductPrice = product.getPrice() - ((appliedMembershipDiscountPercent * 0.01) * product.getPrice());
				discountPercent = appliedMembershipDiscountPercent;
			}

			orderItem.setProduct(product);
			orderItem.setQuantity(cartItem.getQuantity());
			orderItem.setDiscount(discountPercent);
			orderItem.setOrderedProductPrice(orderedProductPrice);
			orderItem.setOrder(savedOrder);

			orderItems.add(orderItem);
		}

		orderItems = orderItemRepo.saveAll(orderItems);

		cart.getCartItems().forEach(item -> {
			int quantity = item.getQuantity();

			Product product = item.getProduct();

			cartService.deleteProductFromCart(cartId, item.getProduct().getProductId());

			product.setQuantity(product.getQuantity() - quantity);
		});

		OrderDTO orderDTO = modelMapper.map(savedOrder, OrderDTO.class);
		
		orderItems.forEach(item -> orderDTO.getOrderItems().add(modelMapper.map(item, OrderItemDTO.class)));

		return orderDTO;
	}

	@Override
	public List<OrderDTO> getOrdersByUser(String email) {
		List<Order> orders = orderRepo.findAllByEmail(email);

		List<OrderDTO> orderDTOs = orders.stream().map(order -> modelMapper.map(order, OrderDTO.class))
				.collect(Collectors.toList());

		if (orderDTOs.size() == 0) {
			throw new APIException("No orders placed yet by the user with email: " + email);
		}

		return orderDTOs;
	}

	@Override
	public OrderDTO getOrder(String email, Long orderId) {

		Order order = orderRepo.findOrderByEmailAndOrderId(email, orderId);

		if (order == null) {
			throw new ResourceNotFoundException("Order", "orderId", orderId);
		}

		return modelMapper.map(order, OrderDTO.class);
	}

	@Override
	public OrderResponse getAllOrders(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {

		Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending()
				: Sort.by(sortBy).descending();

		Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);

		Page<Order> pageOrders = orderRepo.findAll(pageDetails);

		List<Order> orders = pageOrders.getContent();

		List<OrderDTO> orderDTOs = orders.stream().map(order -> modelMapper.map(order, OrderDTO.class))
				.collect(Collectors.toList());
		
		if (orderDTOs.size() == 0) {
			throw new APIException("No orders placed yet by the users");
		}

		OrderResponse orderResponse = new OrderResponse();
		
		orderResponse.setContent(orderDTOs);
		orderResponse.setPageNumber(pageOrders.getNumber());
		orderResponse.setPageSize(pageOrders.getSize());
		orderResponse.setTotalElements(pageOrders.getTotalElements());
		orderResponse.setTotalPages(pageOrders.getTotalPages());
		orderResponse.setLastPage(pageOrders.isLast());
		
		return orderResponse;
	}

	@Override
	public OrderDTO updateOrder(String email, Long orderId, String orderStatus) {

		Order order = orderRepo.findOrderByEmailAndOrderId(email, orderId);

		if (order == null) {
			throw new ResourceNotFoundException("Order", "orderId", orderId);
		}

		order.setOrderStatus(orderStatus);

		return modelMapper.map(order, OrderDTO.class);
	}

}
