package com.noaats.backend.history;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import com.noaats.backend.auth.UserEntity;

@Entity
@Table(name = "promo_history")
public class PromoHistory {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(optional = false)
	@JoinColumn(name = "user_id")
	private UserEntity user;

	@Column(nullable = false)
	private Instant createdAt = Instant.now();

	@Column(nullable = false)
	private long subtotal;

	@Column(nullable = false)
	private long shippingFee;

	@Column(nullable = false)
	private long finalAmount;

	@Column(nullable = false)
	private long totalDiscount;

	@Lob
	@Column(nullable = false)
	private String requestJson;

	@Lob
	@Column(nullable = false)
	private String responseJson;

	protected PromoHistory() {}

	public PromoHistory(UserEntity user,
			long subtotal,
			long shippingFee,
			long finalAmount,
			long totalDiscount,
			String requestJson,
			String responseJson) {
		this.user = user;
		this.subtotal = subtotal;
		this.shippingFee = shippingFee;
		this.finalAmount = finalAmount;
		this.totalDiscount = totalDiscount;
		this.requestJson = requestJson;
		this.responseJson = responseJson;
	}

	public Long getId() {
		return id;
	}

	public UserEntity getUser() {
		return user;
	}

	public Instant getCreatedAt() {
		return createdAt;
	}

	public long getSubtotal() {
		return subtotal;
	}

	public long getShippingFee() {
		return shippingFee;
	}

	public long getFinalAmount() {
		return finalAmount;
	}

	public long getTotalDiscount() {
		return totalDiscount;
	}

	public String getRequestJson() {
		return requestJson;
	}

	public String getResponseJson() {
		return responseJson;
	}
}
