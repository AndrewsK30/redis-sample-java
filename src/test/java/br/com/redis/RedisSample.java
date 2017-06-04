package br.com.redis;

import org.junit.BeforeClass;
import org.junit.Test;
import redis.clients.jedis.Jedis;

import java.util.*;

import static org.fest.assertions.api.Assertions.*;

public class RedisSample {

    private static final String hostname = "localhost";
    //porta 
    private static final int port = 6379;
    //Número do banco que utilizaremos
    private static final int redisDB = 2;
    
    private static final Jedis  jedis = new Jedis(hostname, port);

    @BeforeClass
    public static void cleanupDB() {
        jedis.select(redisDB);
        //Limpa a base de dados
        if (redisDB != 0) {
            jedis.flushDB();
        }
    }

    @Test
    // teste verifica se ha conex
    public void enviadoComandoPingParaVerAConexao() {
        jedis.select(redisDB);
        assertThat(jedis.ping()).isEqualToIgnoringCase("PONG");
    }

    @Test
    public void enseriEBuscaExemplo() throws InterruptedException{
        jedis.select(redisDB);
        
        //Verifica se chave é nula
        assertThat(jedis.get("minhaChave")).isNull();

        //Seta minhaChave com valor "valorQualquer"
        jedis.set("minhaChave", "valorQualquer");

        //verificando o valo de minhaChave
        assertThat(jedis.get("minhaChave")).isNotEmpty();
        assertThat(jedis.get("minhaChave")).isEqualTo("valorQualquer");
        
        //Chave expira em 1 segundo
        jedis.setex("chaveExpiraEm5Segundos", 1, "este valor expirará em 5 segundos");
        assertThat(jedis.get("chaveExpiraEm5Segundos")).isNotNull();
        Thread.sleep(2000l);
        //Após 2 segundos
        assertThat(jedis.get("chaveExpiraEm5Segundos")).isNull();
    }

    @Test
    public void exemploDeLista() {
        jedis.select(redisDB);

        // http://redis.io/commands#list
        assertThat(jedis.llen("minhaLista")).isEqualTo(0L);

        jedis.lpush("minhaLista", "Antonio");
        jedis.lpush("minhaLista", "José");
        jedis.lpush("minhaLista", "Nicolas");
        jedis.lpush("minhaLista", "Zouheir");
        jedis.lpush("minhaLista", "Nicolas");

        //Retorna o tamanho da lista
        assertThat(jedis.llen("minhaLista")).isEqualTo(5L);

        
        //Remove o ultimo da lista e retorna o mesmo
        String ultimoEnseridoERemovidoDaLista = jedis.lpop("minhaLista");
        assertThat(ultimoEnseridoERemovidoDaLista).isEqualTo("Nicolas");
        
        //Retorna o tamanho da lista
        assertThat(jedis.llen("minhaLista")).isEqualTo(4L);

        //Retorna o dado do indice 2 da lista
        assertThat(jedis.lindex("minhaLista", 2)).isEqualTo("José");

        //Corta a lista e deixa somente o último
        jedis.ltrim("minhaLista", 0, 0);

        //Vê se somente há um na lista
        assertThat(jedis.llen("minhaLista")).isEqualTo(1L);

        //Remove o ultimo da lista e retorna o mesmo
        String ultimoEnseridoERemovidoDaLista2 = jedis.lpop("minhaLista");
        
        assertThat(ultimoEnseridoERemovidoDaLista2).isEqualTo("Zouheir");
    }

    @Test
    public void exemploSet() {
        jedis.select(redisDB);

        assertThat(jedis.smembers("nomes")).isEmpty();

        jedis.sadd("nomes", "Nic");
        jedis.sadd("nomes", "Bob");
        jedis.sadd("nomes", "Bob"); // Numero duplicado
        
        //Como tentamos enserir 3 nomes, mas um estava repetido, portanto o total de nomes é 2
        assertThat(jedis.scard("nomes")).isEqualTo(2);

        jedis.sadd("nomes", "Tom");
        jedis.sadd("nomes", "Mike");
        jedis.sadd("nomes", "John");
        jedis.sadd("nomes", "Michel");


        // Cria um segundo set
        jedis.sadd("nomes:nome_frances", "Jean");
        jedis.sadd("nomes:nome_frances", "Michel");

        // Cria um terceiro set
        // Faz uma diferença entre nomes e nomes franceses e coloca em nomes não franceses.
        jedis.sdiffstore("nomes:nome_nao_frances", "nomes", "nomes:nome_frances");

        assertThat(jedis.scard("nomes:nome_nao_frances")).isEqualTo(5);

        //Usando HashSet do java
        HashSet<String> nomesDaUK = new HashSet<String>();
        nomesDaUK.add("Nic");
        nomesDaUK.add("Bob");
        nomesDaUK.add("Tom");
        nomesDaUK.add("Mike");
        nomesDaUK.add("John");

        assertThat(jedis.smembers("nomes:nome_nao_frances")).isEqualTo(nomesDaUK);


        assertThat(jedis.sismember("nomes:nome_nao_frances", "Michel")).isFalse();
        assertThat(jedis.sismember("nomes", "Nic")).isTrue();
    }
    
    @Test
    public void mapExemplo() {
        
        jedis.select(redisDB);

        //Verifica se nome já existe
        assertThat(jedis.exists("nomes:nicolas")).isFalse();

         //Popula o Map
        jedis.hset("nomes:marcelo", "primeiroNome", "marcelo");
        jedis.hset("nomes:marcelo", "ultimoNome", "silva");
        jedis.hset("nomes:marcelo", "twitter", "@marcelo.silva");
        jedis.hset("nomes:marcelo", "bio", "Programador");

        //Verifica se mesmo foi populado
        assertThat(jedis.hget("nomes:marcelo", "primeiroNome")).isEqualTo("marcelo");
        assertThat(jedis.hget("nomes:marcelo", "ultimoNome")).isEqualTo("silva");        
        assertThat(jedis.hget("nomes:marcelo", "twitter")).isEqualTo("@marcelo.silva");

    }


}