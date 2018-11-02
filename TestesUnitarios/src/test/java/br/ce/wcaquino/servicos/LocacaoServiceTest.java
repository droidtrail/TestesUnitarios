package br.ce.wcaquino.servicos;

import static br.ce.wcaquino.utils.DataUtils.isMesmaData;
import static br.ce.wcaquino.utils.DataUtils.obterDataComDiferencaDias;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Date;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
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

	@Rule
	public ErrorCollector error = new ErrorCollector();

	@Rule
	public ExpectedException exception = ExpectedException.none();

	@Test
	public void testeLocacao() throws Exception {

		// Cenário
		LocacaoService service = new LocacaoService();
		Usuario usuario = new Usuario("Usuario 1");
		Filme filme = new Filme("Filme 1", 2, 5.0);

		// Ação
		Locacao locacao = service.alugarFilme(usuario, filme);

		// Verificação
		error.checkThat(locacao.getValor(), is(equalTo(5.0)));
		error.checkThat(isMesmaData(locacao.getDataLocacao(), new Date()), CoreMatchers.is(true));
		error.checkThat(DataUtils.isMesmaData(locacao.getDataRetorno(), obterDataComDiferencaDias(1)), is(true));

	}

	// Elegante
	@Test(expected = FilmeSemEstoqueException.class)
	public void testLocacao_filmeSemEstoque() throws Exception {

		// Cenário
		LocacaoService service = new LocacaoService();
		Usuario usuario = new Usuario("Usuario 1");
		Filme filme = new Filme("Filme 1", 0, 5.0);

		// Ação
		service.alugarFilme(usuario, filme);

	}

	// Robusta
	@Test
	public void testLocacao_usuarioVazio() throws FilmeSemEstoqueException {

		// Cenário
		LocacaoService service = new LocacaoService();
		Filme filme = new Filme("Filme 2", 1, 4.0);

		// Ação

		try {
			service.alugarFilme(null, filme);
			Assert.fail();

		} catch (LocadoraException e) {

			assertThat(e.getMessage(), is("Usuário vazio"));
		}
		
		System.out.println("Forma robusta");

	}

	// Forma nova
	@Test
	public void testLocacao_FilmeVazio() throws FilmeSemEstoqueException, LocadoraException {

		// Cenário
		LocacaoService service = new LocacaoService();
		Usuario usuario = new Usuario("Usuario 1");

		exception.expect(LocadoraException.class);
		exception.expectMessage("Filme vazio");
		
		// Ação
		service.alugarFilme(usuario, null);
		
		System.out.println("Forma nova");
		
	}

}
