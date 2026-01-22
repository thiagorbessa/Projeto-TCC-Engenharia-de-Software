# Sistema de Controle de Acesso - TCC

## 🚀 Sobre o Projeto
Sistema desenvolvido para o Trabalho de Conclusão de Curso em Engenharia de Software. Focado no controle de entrada e saída de pessoas com auditoria completa.

## 🛠️ Tecnologias
* Java 22 / Spring Boot 3
* MySQL 8
* Docker & Docker Compose
* ngrok (para acesso externo)

## 🐳 Como Iniciar (Docker)
1. Certifique-se que o Docker Desktop está rodando.
2. No terminal, execute:
   `docker-compose up -d --build`
3. Acesse em: `http://localhost:8080`

## 🌐 Acesso Externo (Apresentação)
Para gerar o link da internet:
`ngrok http 8080`

## 👥 Perfis de Acesso
* **USER:** Operacional.
* **SISTEMA:** Gerencia usuários básicos.
* **GERAL:** Administrador total.
