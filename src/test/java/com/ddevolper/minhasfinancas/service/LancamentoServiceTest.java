package com.ddevolper.minhasfinancas.service;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.domain.Example;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.ddevolper.minhasfinancas.exception.RegraNegocioException;
import com.ddevolper.minhasfinancas.model.entity.Lancamento;
import com.ddevolper.minhasfinancas.model.entity.StatusLancamento;
import com.ddevolper.minhasfinancas.model.entity.TipoLancamento;
import com.ddevolper.minhasfinancas.model.entity.Usuario;
import com.ddevolper.minhasfinancas.model.repository.LancamentoRepository;
import com.ddevolper.minhasfinancas.model.repository.LancamentoRepositoryTest;
import com.ddevolper.minhasfinancas.service.impl.LancamentoServiceImpl;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class LancamentoServiceTest {

	@SpyBean
	LancamentoServiceImpl service;
	@MockBean
	LancamentoRepository repository;
	
	@Test
	public void deveSalvarUmLancamento() {
		//cenário
		Lancamento lancamentoASalvar= LancamentoRepositoryTest.criarLancamento();
		Mockito.doNothing().when(service).validar(lancamentoASalvar);
		
		Lancamento lancamentoSalvo= LancamentoRepositoryTest.criarLancamento();
		lancamentoSalvo.setId(1l);
		lancamentoSalvo.setStatus(StatusLancamento.PAGO);
		Mockito.when(repository.save(lancamentoASalvar)).thenReturn(lancamentoSalvo);
		
		//execucao
		Lancamento lancamento  = service.salvar(lancamentoASalvar);
		
		//verificacao
		Assertions.assertThat(lancamento.getId()).isEqualTo(lancamentoSalvo.getId());
	}
	
	@Test
	public void naoDeveSalvarUmLancamentoComErroDeValidacao() {
		//cenário
		Lancamento lancamentoASalvar = LancamentoRepositoryTest.criarLancamento();
		Mockito.doThrow(RegraNegocioException.class).when(service).validar(lancamentoASalvar);
		
		//EXECUCAO E VERIFIC
		Assertions.catchThrowableOfType( () -> service.salvar(lancamentoASalvar),RegraNegocioException.class);
		Mockito.verify(repository, Mockito.never()).save(lancamentoASalvar);	
	}
	
	@Test
	public void deveAtualizarLancamento () {
		//cenário
		Lancamento lancamentoSalvo= LancamentoRepositoryTest.criarLancamento();
		lancamentoSalvo.setId(1l);
		lancamentoSalvo.setStatus(StatusLancamento.PENDENTE);
		
		Mockito.doNothing().when(service).validar(lancamentoSalvo);

		Mockito.when(repository.save(lancamentoSalvo)).thenReturn(lancamentoSalvo);
								
		//execucao
		service.atualizar(lancamentoSalvo);
		
		//verificacao
		Mockito.verify(repository, Mockito.times(1)).save(lancamentoSalvo);

	}
	
	@Test
	public void deveLancarErroAoTentarAtualizarLancamentoQueAindaNaoFoiSalvo() {
		//cenário
		Lancamento lancamento = LancamentoRepositoryTest.criarLancamento();
		
		//EXECUCAO E VERIFIC
		Assertions.catchThrowableOfType( () -> service.atualizar(lancamento),NullPointerException.class);
		Mockito.verify(repository, Mockito.never()).save(lancamento);	
	}
	
	@Test
	public void deveDeletarUmLancamento () {
		//cenário
	Lancamento lancamento = LancamentoRepositoryTest.criarLancamento();
	lancamento.setId(1l);
	
	//execucao
	service.deletar(lancamento);
	
	//verificacao
	Mockito.verify(repository).delete(lancamento);
	}
	
	@Test
	public void deveLancarErroAoTentarDeletarLancamentoQueAindaNaoFoiSalvo() {
		//cenário
		Lancamento lancamento = LancamentoRepositoryTest.criarLancamento();
		lancamento.setId(1l);
		
		//execucao
		Assertions.catchThrowableOfType( () -> service.deletar(lancamento),NullPointerException.class);		
		//verificacao
		Mockito.verify(repository, Mockito.never()).delete(lancamento);
	}
	
	@Test
	public void deveFiltrarLancamentos() {
		//cenario
		Lancamento lancamento = LancamentoRepositoryTest.criarLancamento();
		lancamento.setId(1l);
		
		List<Lancamento> lista = Arrays.asList(lancamento);
		Mockito.when(repository.findAll(Mockito.any(Example.class))).thenReturn(lista);
		
		//execucao
		List<Lancamento> resultado = service.buscar(lancamento);
		
		//verificacao
		Assertions.assertThat(resultado)
				   .isNotEmpty()
				   .hasSize(1)
				   .contains(lancamento);
	}
	
	@Test
	public void deveAtualizarStatus() {
		//cenario
		Lancamento lancamento = LancamentoRepositoryTest.criarLancamento();
		lancamento.setId(1l);
		lancamento.setStatus(StatusLancamento.PENDENTE);
		
		StatusLancamento novoStatus = StatusLancamento.EFETIVADO;
		Mockito.doReturn(lancamento).when(service).atualizar(lancamento);
		
		//execucao
		service.atualizarStatus(lancamento, novoStatus);
		
		//verificaçoes
		Assertions.assertThat(lancamento.getStatus()).isEqualTo(novoStatus);
		Mockito.verify(service).atualizar(lancamento);
	}
	
	@Test
	public void deveObterUmLancamentoPorId() {
		//cenario
		Long id = 1l;
		
		Lancamento lancamento = LancamentoRepositoryTest.criarLancamento();
		lancamento.setId(id);
		Mockito.when(repository.findById(id)).thenReturn(Optional.of(lancamento)); 
		
		//execucao
		Optional<Lancamento> resultado = service.obterPorId(id);
		
		//verificacao
		Assertions.assertThat(resultado.isPresent()).isTrue();
	}
	
	@Test
	public void deveRetornarVazioQuandoUmLancamentoNaoExiste() {
		//cenario
		Long id = 1l;
		Lancamento lancamento = LancamentoRepositoryTest.criarLancamento();
		Mockito.when(repository.findById(id)).thenReturn(Optional.empty()); 
		
		//execucao
		Optional<Lancamento> resultado = service.obterPorId(id);
		
		//verificacao
		Assertions.assertThat(resultado.isPresent()).isFalse();
	}
	
	@Test
	public void deveLancarErroAposValidacao() {
		//cenario
		Lancamento lancamento = new Lancamento();
		
		//Execucao e verificacao
			//Descrição
		lancamento.setDescricao(" ");
		Throwable erro = Assertions.catchThrowable( () -> service.validar(lancamento));		
		Assertions.assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe uma descrição válida.");
		
		lancamento.setDescricao(null);
		erro = Assertions.catchThrowable( () -> service.validar(lancamento));
		Assertions.assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe uma descrição válida.");
		
		lancamento.setDescricao("válida.");	

		
			//Mês
		lancamento.setMes(0);
		erro = Assertions.catchThrowable( () -> service.validar(lancamento));
		Assertions.assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um mês valido.");
		
		
		lancamento.setMes(13);
		erro = Assertions.catchThrowable( () -> service.validar(lancamento));
		Assertions.assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um mês valido.");
		
		lancamento.setMes(1);
			
			//Ano
		lancamento.setAno(1);
		erro = Assertions.catchThrowable( () -> service.validar(lancamento));
		Assertions.assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um ano válido.");
		
		lancamento.setAno(2055);
		
			//Usuário
		erro = Assertions.catchThrowable( ()-> service.validar(lancamento));
		Assertions.assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um usuário.");
		
		lancamento.setUsuario(new Usuario());
		
		erro = Assertions.catchThrowable( ()-> service.validar(lancamento));
		Assertions.assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um usuário.");
		
		lancamento.getUsuario().setId(1l);
		
			//Valor
		lancamento.setValor(BigDecimal.ZERO);
		erro = Assertions.catchThrowable(() -> service.validar(lancamento));
		Assertions.assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Insira um valor válido.");
		
		lancamento.setValor(BigDecimal.valueOf(1));
		
		//Tipo
		
		erro = Assertions.catchThrowable(() -> service.validar(lancamento));
		Assertions.assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um tipo de lançamento.");
		lancamento.setTipo(TipoLancamento.DESPESA);
			
	}
}
