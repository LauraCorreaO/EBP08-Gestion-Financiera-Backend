package com.ebp08.gestion_financiera_backend;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class GestionFinancieraBackendApplicationTests {

	@Test
	void applicationStarts() {
		GestionFinancieraBackendApplication app = new GestionFinancieraBackendApplication();
		assertThat(app).isNotNull();
	}

}
