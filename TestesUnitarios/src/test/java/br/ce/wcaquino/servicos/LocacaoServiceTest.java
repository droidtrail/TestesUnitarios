package br.ce.wcaquino.servicos;

import static br.ce.wcaquino.utils.DataUtils.isMesmaData;
import static br.ce.wcaquino.utils.DataUtils.obterDataComDiferencaDias;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;

import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.exceptions.FilmeSemEstoqueException;
import br.ce.wcaquino.exceptions.LocadoraException;
import br.ce.wcaquino.utils.DataUtils;

public class LocacaoServiceTest {

	// Variável global
	private LocacaoService service;

	@Rule
	public ErrorCollector error = new ErrorCollector();

	@Rule
	public ExpectedException exception = ExpectedException.none();

	@Before
	public void setup() {

		service = new LocacaoService();

	}

	@Test
	public void deveAlugarFilme() throws Exception {

		// Cenário
		Usuario usuario = new Usuario("Usuario 1");
		List<Filme> filmes = Arrays.asList(new Filme("Filme 1", 2, 5.0));

		// Ação
		service.alugarFilme(usuario, filmes);
		Locacao locacao = service.alugarFilme(usuario, filmes);

		// Verificação
		error.checkThat(locacao.getValor(), is(equalTo(5.0)));
		error.checkThat(isMesmaData(locacao.getDataLocacao(), new Date()), CoreMatchers.is(true));
		error.checkThat(DataUtils.isMesmaData(locacao.getDataRetorno(), obterDataComDiferencaDias(1)), is(true));

	}

	// Elegante
	@Test(expected = FilmeSemEstoqueException.class)
	public void deveLancarExcecaoAoAlugarFilmeSemEstoque() throws Exception {

		// Cenário

		Usuario usuario = new Usuario("Usuario 1");
		List<Filme> filmes = Arrays.asList(new Filme("Filme 1", 0, 4.0));

		// Ação
		service.alugarFilme(usuario, filmes);

	}

	// Robusta
	@Test
	public void deveAlugarFilmeSemUsuario() throws FilmeSemEstoqueException {

		// Cenário

		List<Filme> filmes = Arrays.asList(new Filme("Filme 1", 2, 4.0));

		// Ação

		try {
			service.alugarFilme(null, filmes);
			Assert.fail();

		} catch (LocadoraException e) {

			assertThat(e.getMessage(), is("Usuário vazio"));
		}

	}

	// Forma nova
	@Test
	public void deveAlugarFilmeSemFilme() throws FilmeSemEstoqueException, LocadoraException {

		// Cenário
		Usuario usuario = new Usuario("Usuario 1");

		exception.expect(LocadoraException.class);
		exception.expectMessage("Filme vazio");

		// Ação
		service.alugarFilme(usuario, null);
	}
	
	@Test
	public void devePagar75PctNoFilme3() throws FilmeSemEstoqueException, LocadoraException {
		//Cenário (Inicializa a lista com 3 filmes)
		Usuario usuario = new Usuario("Usuario 1");
		List<Filme> filmes = Arrays.asList(new Filme("Filme 1", 2, 4.0), new Filme("Filme 2", 2, 4.0), new Filme("Filme 3", 2, 4.0));
		
		//Ação
		
		Locacao resultado = service.alugarFilme(usuario, filmes);
		
		//Verificação
		assertThat(resultado.getValor(), is(11.0));
	}
	
	@Test
	public void devePagar50PctNoFilme4() throws FilmeSemEstoqueException, LocadoraException {
		//Cenário (Inicializa a lista com 4 filmes)
		Usuario usuario = new Usuario("Usuario 1");
		List<Filme> filmes = Arrays.asList(
				new Filme("Filme 1", 2, 4.0), 
				new Filme("Filme 2", 2, 4.0), 
				new Filme("Filme 3", 2, 4.0),
				new Filme("Filme 3", 2, 4.0));
		
		//Ação
		
		Locacao resultado = service.alugarFilme(usuario, filmes);
		
		//Verificação
		assertThat(resultado.getValor(), is(13.0));
	}
	
	@Test
	public void devePagar75PctNoFilme5() throws FilmeSemEstoqueException, LocadoraException {
		//Cenário (Inicializa a lista com 4 filmes)
		Usuario usuario = new Usuario("Usuario 1");
		List<Filme> filmes = Arrays.asList(
				new Filme("Filme 1", 2, 4.0), 
				new Filme("Filme 2", 2, 4.0), 
				new Filme("Filme 3", 2, 4.0),
				new Filme("Filme 4", 2, 4.0),
				new Filme("Filme 3", 2, 4.0));
		
		//Ação
		
		Locacao resultado = service.alugarFilme(usuario, filmes);
		
		//Verificação
		assertThat(resultado.getValor(), is(14.0));
	}
	
	@Test
	public void devePagar100PctNoFilme6() throws FilmeSemEstoqueException, LocadoraException {
		//Cenário (Inicializa a lista com 4 filmes)
		Usuario usuario = new Usuario("Usuario 1");
		List<Filme> filmes = Arrays.asList(
				new Filme("Filme 1", 2, 4.0), 
				new Filme("Filme 2", 2, 4.0), 
				new Filme("Filme 3", 2, 4.0),
				new Filme("Filme 4", 2, 4.0),
				new Filme("Filme 3", 2, 4.0),
				new Filme("Filme 3", 2, 4.0));
		
		//Ação
		
		Locacao resultado = service.alugarFilme(usuario, filmes);
		
		//Verificação
		assertThat(resultado.getValor(), is(14.0));
	}
	
	@Test
	public void deveDevolverNaSegundaAoAlugarNoSabado() throws FilmeSemEstoqueException, LocadoraException {
		
		//Cenário
		Usuario usuario = new Usuario("Usuario 1");
		List<Filme> filmes = Arrays.asList(new Filme("Filme 1", 1, 5.0));
		
		//Ação
		Locacao  retorno = service.alugarFilme(usuario, filmes);
		
		//Verificação
		boolean enSegunda = DataUtils.verificarDiaSemana(retorno.getDataRetorno(), Calendar.MONDAY);
		Assert.assertTrue(enSegunda);
	}

}
