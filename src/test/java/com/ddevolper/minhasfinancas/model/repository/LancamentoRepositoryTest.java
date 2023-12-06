package com.ddevolper.minhasfinancas.model.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.ddevolper.minhasfinancas.model.entity.Lancamento;
import com.ddevolper.minhasfinancas.model.entity.StatusLancamento;
import com.ddevolper.minhasfinancas.model.entity.TipoLancamento;

@SuppressWarnings("deprecation")
@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@ActiveProfiles
public class LancamentoRepositoryTest {

	@Autowired 
	 LancamentoRepository repository;
	
	@Autowired 
	TestEntityManager entityManager;
	
	@Test
	public void deveSalvar() {
		Lancamento lancamento = criarLancamento();
		
		lancamento = repository.save(lancamento);
		
		Assertions.assertThat(lancamento.getId()).isNotNull(); 
	}
		
		@Test
		public void deveBuscarUmLancamentoPorId() {
			Lancamento lancamento = criarEPersistirUmLancamento();
			
			Optional<Lancamento> lancamentoEncontrado = repository.findById(lancamento.getId());
			
			assertThat(lancamentoEncontrado.isPresent()).isTrue();
		}
		
		@Test
		public void deveDeletarUmLancamento() {
			Lancamento lancamento = criarEPersistirUmLancamento();
			
			entityManager.find(Lancamento.class, lancamento.getId());
			
			repository.delete(lancamento);
			
			Lancamento lancamentoInexistente = entityManager.find(Lancamento.class, lancamento.getId());
			Assertions.assertThat(lancamentoInexistente).isNull();
		}
	
		@Test
		public void deveAtualizarUmLancamento() {
			Lancamento lancamento = criarEPersistirUmLancamento();
			
			lancamento.setAno(2018);
			lancamento.setDescricao("Teste Atualizar");
			lancamento.setStatus(StatusLancamento.CANCELADO);
			repository.save(lancamento);
			
			Lancamento lancamentoAtualizado = entityManager.find(Lancamento.class, lancamento.getId());
			assertThat(lancamentoAtualizado.getAno()).isEqualTo(2018);
			assertThat(lancamentoAtualizado.getDescricao()).isEqualTo("Teste Atualizar");
			assertThat(lancamentoAtualizado.getStatus()).isEqualTo(StatusLancamento.CANCELADO);
		}
		
		
		 public static Lancamento criarLancamento () {
		 return Lancamento.builder()
												.ano(2019)
												.mes(1)
												.descricao("lançamento qualquer.")
												.valor(BigDecimal.valueOf(10))
												.status(StatusLancamento.PENDENTE)
												.tipo(TipoLancamento.DESPESA)
												.data(LocalDate.now())
												.build();
	}
	
		private Lancamento criarEPersistirUmLancamento() {
			Lancamento lancamento = criarLancamento();
			entityManager.persist(lancamento);
			return lancamento;
		}
	
}
