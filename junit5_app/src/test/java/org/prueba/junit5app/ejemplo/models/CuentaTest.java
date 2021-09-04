package org.prueba.junit5app.ejemplo.models;

import jdk.jfr.Name;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.condition.*;
import org.prueba.junit5app.ejemplo.exceptions.DineroInsuficienteException;

import java.math.BigDecimal;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Esto es creado por Jahir SR M
 * **/

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CuentaTest {

    Cuenta cuenta;

    @BeforeEach
    public void initMetodoTest() {
        this.cuenta=new Cuenta("Jahir", new BigDecimal("1000.12345"));

        System.out.println("Iniciando el metodo.");
    }

    @AfterEach
    public void tearDown() {
        System.out.println("Finalizando el metodo");
    }

    @BeforeAll //en caso no se tenga la notacion TestInstance al inicio se deberá usar metodo estatico
    public void beforeAll() {
        System.out.println("Inicializando el Test");
    }

    @AfterAll
    public void afterAll() {
        System.out.println("Finalizando el Test");
    }

    @Test
    @DisplayName("Probando nombre de la cuenta corriente")
    public void testNombreCuenta() {

//        cuenta.setPersona("Jahir");
        String esperado = "Jahir";
        String real = cuenta.getPersona();
        assertNotNull(real,()->"La cuenta no puede ser nula");
        assertNotNull(esperado,()->"El nombre de la cuenta no es el que se esperaba");
        Assertions.assertEquals(esperado, real,
                ()->"El nombre de la cuenta no es el que se esperaba: se esperaba "+ esperado
                        + " sin embargo fue "+ real);
        Assertions.assertTrue(real.equals(esperado),
                ()-> "El nombre de la cuenta deber ser igual al real");
    }

    @Test
    @DisplayName("Probando el saldo de la cuenta corriente, que no sea null, mayor que cero, valor esperado")
    public void testSaldoCuenta() {
        assertNotNull(cuenta.getSaldo());
        assertEquals(1000.12345, cuenta.getSaldo().doubleValue());
        assertFalse(cuenta.getSaldo().compareTo(BigDecimal.ZERO) < 0);
        assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) > 0);
    }

    @Test
    @DisplayName("Testeando referencia que sean iguales con el metodo equals")
    public void testTeferenciaCuenta() {
        cuenta = new Cuenta("Jahir SR", new BigDecimal("8900.9997"));
        Cuenta cuenta2 = new Cuenta("Jahir SR", new BigDecimal("8900.9997"));
        //assertNotEquals(cuenta2,cuenta);
        assertEquals(cuenta2, cuenta);
    }

    @Test
    @DisplayName("Probando el monto nuevo con el metodo debito")
    public void testDebitoCuenta() {
        cuenta.debito(new BigDecimal(100));
        assertNotNull(cuenta.getSaldo());
        assertEquals(900, cuenta.getSaldo().intValue());
        assertEquals("900.12345", cuenta.getSaldo().toPlainString());
    }

    @Test
    @DisplayName("Probando el monto nuevo con el metodo credito")
    public void testCreditoCuenta() {
        cuenta.credito(new BigDecimal(100));
        assertNotNull(cuenta.getSaldo());
        assertEquals(1100, cuenta.getSaldo().intValue());
        assertEquals("1100.12345", cuenta.getSaldo().toPlainString());
    }

    @Test
    @DisplayName("Manejo d excepcion para dinero insuficiente")
    public void testDineroInsufucienteExceptioCuenta() {
        Exception exception = assertThrows(DineroInsuficienteException.class, () -> {
            cuenta.debito(new BigDecimal(1500));
        });
        String actual = exception.getMessage();
        String esperado = "Dinero Insuficiente";
        assertEquals(esperado, actual);
    }

    @Test
    @DisplayName("Probando los montos nuevos con el metodo transferir")
    public void testTranferirDineroCuentas() {
        Cuenta cuenta = new Cuenta("Jahir SR", new BigDecimal("2500"));
        Cuenta cuenta2 = new Cuenta("Jhan SR", new BigDecimal("1500.8989"));
        Banco banco = new Banco();
        banco.setNombre("Bnaco del Estado");
        banco.transferir(cuenta2, cuenta, new BigDecimal(500));
        assertEquals("1000.8989", cuenta2.getSaldo().toPlainString());
        assertEquals("3000", cuenta.getSaldo().toPlainString());
    }

    @Test
    @DisplayName("Probando relaciones entre las cuenta u el banco con assertAll")
    public void testRelacionBancoCuentas() {
        Cuenta cuenta = new Cuenta("Jahir SR", new BigDecimal("2500"));
        Cuenta cuenta2 = new Cuenta("Jhan SR", new BigDecimal("1500.8989"));

        Banco banco = new Banco();
        banco.addCuenta(cuenta);
        banco.addCuenta(cuenta2);

        banco.setNombre("Bnaco del Estado");
        banco.transferir(cuenta2, cuenta, new BigDecimal(500));
        assertAll(() -> {
            assertEquals("1000.8989", cuenta2.getSaldo().toPlainString(),
                    ()->"El valor del saldo de la cuenta2 no es el esperado");
        }, () -> {
            assertEquals("3000", cuenta.getSaldo().toPlainString(),
                    ()->"El valor del saldo de la cuenta1 no es el esperado");
        }, () -> {
            assertEquals(2, banco.getCuentas().size(),
                    ()->"El banco no tiene las cuentas esperadas");
        }, () -> {
            assertEquals("Bnaco del Estado", cuenta.getBanco().getNombre());
        }, () -> {
            assertEquals("Jahir SR", banco.getCuentas().stream()
                    .filter(c -> c.getPersona().equals("Jahir SR"))
                    .findFirst()
                    .get().getPersona());
        }, () -> {
            assertTrue(banco.getCuentas().stream()
                    .anyMatch(c -> c.getPersona().equals("Jahir SR")));
        });
    }

    @Test
    @EnabledOnOs(OS.WINDOWS)
    public void testSoloWindows() {
        System.out.println("Ejecution en Win");
    }

    @Test
    @EnabledOnOs({OS.LINUX, OS.MAC})
    public void testSoloLinuxMac() {
        System.out.println("Ejecution en Linux y Mac");
    }

    @Test
    @DisabledOnOs(OS.WINDOWS)
    public void testNoWindows() {
        System.out.println("Ejecution cuando no es windows");
    }

    @Test
    @EnabledOnJre(JRE.JAVA_8)
    public void soloJdk8() {
        System.out.println("Solo ejecuta con java 8");
    }

    @Test
    @EnabledOnJre(JRE.JAVA_15)
    public void soloJdk15() {
        System.out.println("Solo ejecuta con java 15");
    }

    @Test
    @DisabledOnJre(JRE.JAVA_15)
    public void soloNoJdk15() {
        System.out.println("Solo ejecuta con no es java 15");
    }

    @Test
    public void imprimirSystemProperties() {
        Properties properties = System.getProperties();
        properties.forEach((k, v)-> System.out.println(k + ":" +v));
    }

    @Test
    @EnabledIfSystemProperty(named = "java.version", matches = ".*11.*")
    public void testJavaVersion() {
    }

    @Test
    @DisabledIfSystemProperty(named = "os.arch", matches = ".*32.*")
    public void testSolo64() {}

    @Test
    @EnabledIfSystemProperty(named = "os.arch", matches = ".*32.*")
    public void testNo64() {}

}