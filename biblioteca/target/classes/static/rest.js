const API_URL = "http://localhost:8080/api";

let criandoNovoLivro = false;
let usuarioLogadoId = null;
let usuarioLogadoRegistro = null;

document.addEventListener("DOMContentLoaded", inicializar);

function inicializar() {
    garantirAreaMensagem();
    listarLivros();
    listarUsuarios();
    listarEmprestimos();
}

function garantirAreaMensagem() {
    let mensagem = document.querySelector("#paragrafoMensagem");

    if (!mensagem) {
        mensagem = document.createElement("p");
        mensagem.id = "paragrafoMensagem";
        mensagem.className = "mensagem";
        mensagem.textContent = "Bem-vindo ao sistema da biblioteca.";

        const main = document.querySelector("main");
        main.insertBefore(mensagem, main.firstChild);
    }
}

function mostrarMensagem(texto) {
    const mensagem = document.querySelector("#paragrafoMensagem");
    if (mensagem) {
        mensagem.textContent = texto;
    }
}

function mostrarSecao(idSecao) {
    document.querySelectorAll("main section").forEach(secao => {
        secao.classList.add("oculto");
    });

    document.querySelector("#" + idSecao).classList.remove("oculto");
}

// =========================
// LIVROS
// =========================

async function listarLivros() {
    try {
        const resposta = await fetch(`${API_URL}/livros`);

        if (!resposta.ok) {
            throw new Error("Erro ao buscar livros");
        }

        const livros = await resposta.json();

        const corpo = document.querySelector("#corpoTabelaLivros");
        corpo.innerHTML = "";

        livros.forEach(livro => {
            const linha = corpo.insertRow();

            linha.insertCell().textContent = livro.codigoLivro;
            linha.insertCell().textContent = livro.titulo;
            linha.insertCell().textContent = livro.autor;
            linha.insertCell().textContent = livro.anoDePublicacao;

            const celulaAcao = linha.insertCell();

            const botaoEditar = document.createElement("button");
            botaoEditar.textContent = "Editar";
            botaoEditar.className = "acao btn-novo";
            botaoEditar.onclick = () => selecionarLivro(livro.codigoLivro);

            celulaAcao.appendChild(botaoEditar);
        });

    } catch (erro) {
        mostrarMensagem("Erro ao listar livros.");
    }
}

async function selecionarLivro(codigoLivro) {
    try {
        const resposta = await fetch(`${API_URL}/livros/${codigoLivro}`);

        if (!resposta.ok) {
            throw new Error("Livro não encontrado");
        }

        const livro = await resposta.json();

        criandoNovoLivro = false;

        document.querySelector("#txtCodigoLivro").value = livro.codigoLivro;
        document.querySelector("#txtTituloLivro").value = livro.titulo;
        document.querySelector("#txtAutorLivro").value = livro.autor;
        document.querySelector("#txtAnoLivro").value = livro.anoDePublicacao;

        document.querySelector("#formLivro").classList.remove("oculto");

        mostrarMensagem("Altere os dados do livro e clique em Salvar.");

    } catch (erro) {
        mostrarMensagem("Erro ao selecionar livro.");
    }
}

function novoLivro() {
    criandoNovoLivro = true;

    document.querySelector("#txtCodigoLivro").value = "";
    document.querySelector("#txtTituloLivro").value = "";
    document.querySelector("#txtAutorLivro").value = "";
    document.querySelector("#txtAnoLivro").value = "";

    document.querySelector("#formLivro").classList.remove("oculto");

    mostrarMensagem("Preencha os dados do novo livro.");
}

async function salvarLivro() {
    const codigoLivro = document.querySelector("#txtCodigoLivro").value;

    const dadosLivro = {
        titulo: document.querySelector("#txtTituloLivro").value,
        autor: document.querySelector("#txtAutorLivro").value,
        anoDePublicacao: Number(document.querySelector("#txtAnoLivro").value)
    };

    if (!dadosLivro.titulo || !dadosLivro.autor || dadosLivro.anoDePublicacao <= 0) {
        mostrarMensagem("Preencha todos os campos do livro corretamente.");
        return;
    }

    try {
        if (criandoNovoLivro) {
            const resposta = await fetch(`${API_URL}/livros`, {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(dadosLivro)
            });

            if (!resposta.ok) {
                throw new Error("Erro ao cadastrar livro");
            }

            mostrarMensagem("Livro cadastrado com sucesso.");
        } else {
            const resposta = await fetch(`${API_URL}/livros/${codigoLivro}`, {
                method: "PUT",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(dadosLivro)
            });

            if (!resposta.ok) {
                throw new Error("Erro ao alterar livro");
            }

            mostrarMensagem("Livro alterado com sucesso.");
        }

        cancelarLivro();
        listarLivros();

    } catch (erro) {
        mostrarMensagem("Erro ao salvar livro.");
    }
}

async function apagarLivro() {
    const codigoLivro = document.querySelector("#txtCodigoLivro").value;

    if (!codigoLivro) {
        mostrarMensagem("Selecione um livro para apagar.");
        return;
    }

    try {
        const resposta = await fetch(`${API_URL}/livros/${codigoLivro}`, {
            method: "DELETE"
        });

        if (!resposta.ok) {
            throw new Error("Erro ao apagar livro");
        }

        mostrarMensagem("Livro apagado com sucesso.");
        cancelarLivro();
        listarLivros();

    } catch (erro) {
        mostrarMensagem("Erro ao apagar livro.");
    }
}

function cancelarLivro() {
    document.querySelector("#formLivro").classList.add("oculto");
    criandoNovoLivro = false;
}

// =========================
// USUÁRIOS
// =========================

async function listarUsuarios() {
    try {
        const resposta = await fetch(`${API_URL}/usuarios`);

        if (!resposta.ok) {
            throw new Error("Erro ao buscar usuários");
        }

        const usuarios = await resposta.json();

        const corpo = document.querySelector("#corpoTabelaUsuarios");
        corpo.innerHTML = "";

        usuarios.forEach(usuario => {
            const linha = corpo.insertRow();

            linha.insertCell().textContent = usuario.id;
            linha.insertCell().textContent = usuario.nome;
            linha.insertCell().textContent = usuario.dataNascimento;
            linha.insertCell().textContent = usuario.telefone;
            linha.insertCell().textContent = usuario.email;
        });

    } catch (erro) {
        mostrarMensagem("Erro ao listar usuários.");
    }
}

function novoUsuario() {
    document.querySelector("#txtIdUsuario").value = "";
    document.querySelector("#txtNomeUsuario").value = "";
    document.querySelector("#txtDataNascimentoUsuario").value = "";
    document.querySelector("#txtTelefoneUsuario").value = "";

    document.querySelector("#formUsuario").classList.remove("oculto");

    mostrarMensagem("Preencha os dados do novo usuário.");
}

async function salvarUsuario() {
    const nome = document.querySelector("#txtNomeUsuario").value;
    const dataNascimento = document.querySelector("#txtDataNascimentoUsuario").value;
    const telefone = document.querySelector("#txtTelefoneUsuario").value;

    let registro = document.querySelector("#txtRegistroUsuario")?.value;
    let senha = document.querySelector("#txtSenhaUsuario")?.value;

    if (!registro) {
        registro = prompt("Digite o registro do usuário:");
    }

    if (!senha) {
        senha = prompt("Digite uma senha para o usuário:");
    }

    const dadosUsuario = {
        nome: nome,
        dataNascimento: dataNascimento,
        telefone: telefone,
        registro: Number(registro),
        senha: senha
    };

    if (!dadosUsuario.nome || !dadosUsuario.dataNascimento || !dadosUsuario.telefone) {
        mostrarMensagem("Preencha nome, data de nascimento e telefone.");
        return;
    }

    if (!dadosUsuario.registro || dadosUsuario.registro <= 0) {
        mostrarMensagem("Registro inválido.");
        return;
    }

    if (!dadosUsuario.senha) {
        mostrarMensagem("Senha obrigatória.");
        return;
    }

    try {
        const resposta = await fetch(`${API_URL}/usuarios`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(dadosUsuario)
        });

        if (!resposta.ok) {
            const erroTexto = await resposta.text();
            mostrarMensagem(erroTexto || "Erro ao salvar usuário.");
            return;
        }

        mostrarMensagem("Usuário cadastrado com sucesso.");
        cancelarUsuario();
        listarUsuarios();

    } catch (erro) {
        mostrarMensagem("Erro ao salvar usuário.");
    }
}

function cancelarUsuario() {
    document.querySelector("#formUsuario").classList.add("oculto");
}

// =========================
// LOGIN DO USUÁRIO
// =========================

async function loginUsuario() {
    let registro = document.querySelector("#txtLoginUsuario").value;
    let senha = document.querySelector("#txtLoginSenha")?.value;

    if (!registro) {
        mostrarMensagem("Digite o registro do usuário.");
        return;
    }

    if (!senha) {
        senha = prompt("Digite a senha do usuário:");
    }

    try {
        const resposta = await fetch(`${API_URL}/usuarios/login`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({
                registro: Number(registro),
                senha: senha
            })
        });

        if (!resposta.ok) {
            mostrarMensagem("Registro ou senha inválidos.");
            return;
        }

        const usuario = await resposta.json();

        usuarioLogadoId = usuario.id;
        usuarioLogadoRegistro = usuario.registro;

        document.querySelector("#areaUsuarioLogado").classList.remove("oculto");
        document.querySelector("#tituloUsuarioLogado").textContent =
            `Usuário logado: ${usuario.nome} - ${usuario.email}`;

        listarEmprestimosDoUsuario(usuarioLogadoId);

        mostrarMensagem("Login realizado com sucesso.");

    } catch (erro) {
        mostrarMensagem("Erro ao realizar login.");
    }
}

async function listarEmprestimosDoUsuario(idUsuario) {
    try {
        const resposta = await fetch(`${API_URL}/emprestimos/usuario/${idUsuario}`);

        if (!resposta.ok) {
            throw new Error("Erro ao buscar empréstimos do usuário");
        }

        const emprestimos = await resposta.json();

        const corpo = document.querySelector("#corpoTabelaEmprestimosUsuario");
        corpo.innerHTML = "";

        emprestimos.forEach(emprestimo => {
            const linha = corpo.insertRow();

            linha.insertCell().textContent = emprestimo.codigoEmprestimo;
            linha.insertCell().textContent = emprestimo.exemplar
                ? emprestimo.exemplar.codigoExemplar
                : "";
            linha.insertCell().textContent = emprestimo.dataEmprestimo;
            linha.insertCell().textContent = emprestimo.dataPrevistaDevolucao;

            const celulaAcao = linha.insertCell();

            const botaoDevolver = document.createElement("button");
            botaoDevolver.textContent = "Devolver";
            botaoDevolver.className = "acao btn-apagar";
            botaoDevolver.onclick = () => devolverEmprestimo(emprestimo.codigoEmprestimo);

            const botaoProlongar = document.createElement("button");
            botaoProlongar.textContent = "Prolongar";
            botaoProlongar.className = "acao btn-novo";
            botaoProlongar.onclick = () => prolongarEmprestimo(emprestimo.codigoEmprestimo);

            celulaAcao.appendChild(botaoDevolver);
            celulaAcao.appendChild(document.createTextNode(" "));
            celulaAcao.appendChild(botaoProlongar);
        });

    } catch (erro) {
        mostrarMensagem("Erro ao listar empréstimos do usuário.");
    }
}

// =========================
// EMPRÉSTIMOS
// =========================

async function registrarEmprestimo() {
    const registroUsuario = document.querySelector("#txtEmprestimoUsuario").value;
    const codigoExemplar = document.querySelector("#txtEmprestimoExemplar").value;

    const dadosEmprestimo = {
        registroUsuario: Number(registroUsuario),
        codigoExemplar: Number(codigoExemplar)
    };

    if (!dadosEmprestimo.registroUsuario || !dadosEmprestimo.codigoExemplar) {
        mostrarMensagem("Preencha o registro do usuário e o código do exemplar.");
        return;
    }

    try {
        const resposta = await fetch(`${API_URL}/emprestimos`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(dadosEmprestimo)
        });

        if (!resposta.ok) {
            const erroTexto = await resposta.text();
            mostrarMensagem(erroTexto || "Erro ao registrar empréstimo.");
            return;
        }

        mostrarMensagem("Empréstimo registrado com sucesso.");
        listarEmprestimos();

        if (usuarioLogadoId != null) {
            listarEmprestimosDoUsuario(usuarioLogadoId);
        }

    } catch (erro) {
        mostrarMensagem("Erro ao registrar empréstimo.");
    }
}

async function listarEmprestimos() {
    try {
        const resposta = await fetch(`${API_URL}/emprestimos`);

        if (!resposta.ok) {
            throw new Error("Erro ao buscar empréstimos");
        }

        const emprestimos = await resposta.json();

        const corpo = document.querySelector("#corpoTabelaEmprestimos");
        corpo.innerHTML = "";

        emprestimos.forEach(emprestimo => {
            const linha = corpo.insertRow();

            linha.insertCell().textContent = emprestimo.codigoEmprestimo;
            linha.insertCell().textContent = emprestimo.usuario
                ? emprestimo.usuario.nome
                : "";
            linha.insertCell().textContent = emprestimo.exemplar
                ? emprestimo.exemplar.codigoExemplar
                : "";
            linha.insertCell().textContent = emprestimo.dataEmprestimo;
            linha.insertCell().textContent = emprestimo.dataPrevistaDevolucao;
            linha.insertCell().textContent = emprestimo.dataDevolvida || "Em aberto";

            const celulaAcao = linha.insertCell();

            const botaoDevolver = document.createElement("button");
            botaoDevolver.textContent = "Devolver";
            botaoDevolver.className = "acao btn-apagar";
            botaoDevolver.onclick = () => devolverEmprestimo(emprestimo.codigoEmprestimo);

            const botaoProlongar = document.createElement("button");
            botaoProlongar.textContent = "Prolongar";
            botaoProlongar.className = "acao btn-novo";
            botaoProlongar.onclick = () => prolongarEmprestimo(emprestimo.codigoEmprestimo);

            celulaAcao.appendChild(botaoDevolver);
            celulaAcao.appendChild(document.createTextNode(" "));
            celulaAcao.appendChild(botaoProlongar);
        });

    } catch (erro) {
        mostrarMensagem("Erro ao listar empréstimos.");
    }
}

async function devolverEmprestimo(codigoEmprestimo) {
    try {
        const resposta = await fetch(`${API_URL}/emprestimos/${codigoEmprestimo}/devolver`, {
            method: "PUT"
        });

        if (!resposta.ok) {
            const erroTexto = await resposta.text();
            mostrarMensagem(erroTexto || "Erro ao devolver empréstimo.");
            return;
        }

        mostrarMensagem("Devolução realizada com sucesso.");

        listarEmprestimos();

        if (usuarioLogadoId != null) {
            listarEmprestimosDoUsuario(usuarioLogadoId);
        }

    } catch (erro) {
        mostrarMensagem("Erro ao devolver empréstimo.");
    }
}

async function prolongarEmprestimo(codigoEmprestimo) {
    const dias = prompt("Quantos dias deseja prolongar?", "7");

    if (!dias) {
        return;
    }

    try {
        const resposta = await fetch(`${API_URL}/emprestimos/${codigoEmprestimo}/prolongar?dias=${dias}`, {
            method: "PUT"
        });

        if (!resposta.ok) {
            const erroTexto = await resposta.text();
            mostrarMensagem(erroTexto || "Erro ao prolongar empréstimo.");
            return;
        }

        mostrarMensagem("Empréstimo prolongado com sucesso.");

        listarEmprestimos();

        if (usuarioLogadoId != null) {
            listarEmprestimosDoUsuario(usuarioLogadoId);
        }

    } catch (erro) {
        mostrarMensagem("Erro ao prolongar empréstimo.");
    }
}