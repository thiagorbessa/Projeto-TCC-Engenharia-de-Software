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
2. No terminal da pasta do projeto, execute:
   `docker-compose up -d --build`
3. Acesse em: `http://localhost:8080`

## 🌐 Acesso Externo (Apresentação)
Para gerar o link da internet para a banca:
1. Com o Docker já rodando, abra um novo terminal e digite:
   `ngrok http 8080`
2. Copie a URL `https://...` gerada no campo *Forwarding*.

## 🛑 Como Finalizar
Para encerrar os serviços de forma segura e liberar os recursos (portas e memória) da máquina:
1. **No ngrok:** Pressione `Ctrl + C` no terminal onde o túnel está aberto.
2. **No Docker:** No terminal da pasta do projeto, execute:
   `docker-compose down`
   *(Este comando para os containers e remove a rede virtual, mantendo os dados salvos nos volumes).*

## 📊 Monitoramento (Opcional)
Para visualizar o que está acontecendo no "coração" do sistema (logs do Java) enquanto você navega:
`docker logs -f spring_app`

## 👥 Perfis de Acesso
* **USER:** Operacional. Registro de entrada/saída e cadastro de pessoas.
* **SISTEMA:** Admin Nível 1. Gerencia usuários operacionais e visualiza auditoria.
* **GERAL:** Admin Nível 2. Controle total de todos os usuários e configurações.
