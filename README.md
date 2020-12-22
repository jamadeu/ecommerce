<h1 align="center">ECOMMERCE</h1>

![GitHub Workflow Status](https://img.shields.io/github/workflow/status/jamadeu/ecommerce/Java%20CI)

## Sobre

Projeto de um ecommerce desenvolvido com java + spring.

## Executando o projeto com Docker

* Requisito: [Docker](https://docs.docker.com/get-docker/)

Execute o docker, abra o terminal em '.../GoBarber_backend_java' e execute o comando:

```sh
docker-compose up
```

## Executando o projeto localmente

* Requisito: [Maven](https://maven.apache.org/download.cgi)

Abra o terminal em '.../ecommerce' e execute:

```sh
./mvnw clean install
```

Após terminal a instalação, execute:

```sh
./mvnw spring-boot:run
```

## Documentação

Para documentação deste projeto foi utilizado o framework Swagger.

Com os serviços em execução, a documentação das API estará disponível em:

http://localhost:8080/swagger-ui.html

