# Exemplos de comandos de Redis em java com Jedis e Junit.

Este repositório tem como objetivo apenas exemplificar alguns comandos de Redis no java.
### Requisitos
* Maven
* Java
* Redis
## Instalação do Redis

Para linux ou OSX
```
wget http://download.redis.io/redis-stable.tar.gz
tar xvzf redis-stable.tar.gz
cd redis-stable
make
```
Para windows

[Baixe o zip](https://github.com/MSOpenTech/redis/releases)

## Rodando o servidor do Redis

Para Linux ou OSX e rode o comando:

```
redis-server
```

Para windows execute

```
redis-server.exe
```

Para executar consultas no redis, Linux ou OSX:

```
redis-cli
```

Para windows:

```
redis-cli.exe
```