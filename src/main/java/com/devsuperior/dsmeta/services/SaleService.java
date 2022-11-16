package com.devsuperior.dsmeta.services;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.devsuperior.dsmeta.dto.SaleReportDTO;
import com.devsuperior.dsmeta.dto.SaleSummaryDTO;
import com.devsuperior.dsmeta.projections.SalesReportProjection;
import com.devsuperior.dsmeta.projections.SalesSummaryProjection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.devsuperior.dsmeta.dto.SaleMinDTO;
import com.devsuperior.dsmeta.entities.Sale;
import com.devsuperior.dsmeta.repositories.SaleRepository;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SaleService {

	@Autowired
	private SaleRepository repository;

	@Transactional(readOnly = true)
	public SaleMinDTO findById(Long id) {
		Optional<Sale> result = repository.findById(id);
		Sale entity = result.get();
		return new SaleMinDTO(entity);
	}

	public List<SaleSummaryDTO> getSummary(String minDate, String maxDate) {
		LocalDate today = LocalDate.ofInstant(Instant.now(), ZoneId.systemDefault());
		LocalDate lastYear = today.minusYears(1L);

		LocalDate min = "".equals(minDate) ? lastYear : LocalDate.parse(minDate);
		LocalDate max = "".equals(maxDate) ? today : LocalDate.parse(maxDate);

		List<SalesSummaryProjection> result = repository.summary(min, max);
		return result.stream().map(x -> new SaleSummaryDTO(x)).collect(Collectors.toList());
	}

	public List<SaleReportDTO> getReport(String minDate, String maxDate, String namePart) {
		LocalDate today = LocalDate.ofInstant(Instant.now(), ZoneId.systemDefault());
		LocalDate lastYear = today.minusYears(1L);

		LocalDate min = "".equals(minDate) ? lastYear : LocalDate.parse(minDate);
		LocalDate max = "".equals(maxDate) ? today : LocalDate.parse(maxDate);

		List<SalesReportProjection> result = repository.report(min, max, namePart);
		return result.stream().map(x -> new SaleReportDTO(x)).collect(Collectors.toList());
	}
}
