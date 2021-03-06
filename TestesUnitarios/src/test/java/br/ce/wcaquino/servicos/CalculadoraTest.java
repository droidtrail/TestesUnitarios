package br.ce.wcaquino.servicos;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import br.ce.wcaquino.exceptions.NaoPodeDividirPorZeroException;

public class CalculadoraTest {

	private Calculadora calc;

	@Before
	public void setup() {

		calc = new Calculadora();

	}

	@Test
	public void deveSomarDoisValores() {
		// Cen�rio

		int a = 5;
		int b = 3;

		// A��o

		int resultado = calc.somar(a, b);

		// Verifica��o
		Assert.assertEquals(8, resultado);

	}

	@Test
	public void deveSubtrairDoisValores() {

		// Cen�rio

		int a = 5;
		int b = 3;

		// A��o

		int resultado = calc.subtrair(a, b);

		// Verifica��o

		Assert.assertEquals(2, resultado);

	}

	@Test
	public void deveDividirDoisValores() throws NaoPodeDividirPorZeroException {

		// Cen�rio

		int a = 8;
		int b = 4;

		// A��o

		int resultado = calc.dividir(a, b);

		// Verifica��o

		Assert.assertEquals(2, resultado);
	}

	@Test(expected = NaoPodeDividirPorZeroException.class)
	public void deveLancarExcecaoAoDividirPorZero() throws NaoPodeDividirPorZeroException {
		// Cen�rio

		int a = 10;
		int b = 0;

		// A��o

		int resultado = calc.dividir(a, b);
	}

}
