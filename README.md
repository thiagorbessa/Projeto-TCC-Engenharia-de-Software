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

## 🏠 Acesso em Rede Local (Sem Internet/ngrok)
Se o objetivo for rodar o sistema apenas para dispositivos que estão na mesma rede Wi-Fi/Cabo (Intranet), sem expor o sistema para a internet:

1. **Descubra o IP do Servidor:** No computador onde o Docker está rodando, abra o terminal e digite `ipconfig`. Procure pelo "Endereço IPv4" (ex: `192.168.1.15`).
2. **Acesso por outros dispositivos:** Em qualquer outro computador, celular ou tablet conectado à mesma rede, abra o navegador e digite:
   `http://[IP_DO_SERVIDOR]:8080`
   *(Exemplo: http://192.168.1.15:8080)*
3. **Vantagem:** Este método é mais rápido e seguro para uso interno, pois os dados não saem da rede local da instituição/empresa.

## 🌐 Acesso Externo 
Caso precise que alguém fora da sua rede local (via Internet) acesse o sistema:
1. Com o Docker já rodando, abra um novo terminal e digite:
   `ngrok http 8080`
2. Copie a URL `https://...` gerada no campo *Forwarding*.

## 🛑 Como Finalizar
Para encerrar os serviços de forma segura e liberar os recursos da máquina:
1. **No ngrok:** Pressione `Ctrl + C` no terminal do túnel.
2. **No Docker:** No terminal da pasta do projeto, execute:
   `docker-compose down`

## 📊 Monitoramento (Opcional)
Para visualizar o processamento em tempo real (logs do Java):
`docker logs -f spring_app`

## 👥 Perfis de Acesso
* **USER:** Operacional. Registro de entrada/saída e cadastro de pessoas.
* **SISTEMA:** Admin Nível 1. Gerencia usuários operacionais e auditoria básica.
* **GERAL:** Admin Nível 2. Controle total de usuários e configurações do sistema.
