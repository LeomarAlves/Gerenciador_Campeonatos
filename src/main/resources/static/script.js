// ==========================================
// VARIÁVEIS GLOBAIS (A memória do sistema)
// ==========================================
let campeonatoAtivoId = null;
let categoriaAtivaId = null;
let bateriaAtivaId = null;

// ==========================================
// INICIALIZAÇÃO (Quando a página carrega)
// ==========================================
document.addEventListener('DOMContentLoaded', () => {
    carregarCampeonatos();
    carregarOpcoesTabelas(); // Carrega as regras de pontuação pro Select

    // Formulários (Salvar Dados)
    document.getElementById('form-campeonato').addEventListener('submit', salvarCampeonato);
    document.getElementById('form-categoria').addEventListener('submit', salvarCategoria);
    document.getElementById('form-piloto').addEventListener('submit', salvarPiloto);
    document.getElementById('form-bateria').addEventListener('submit', salvarBateria);
    document.getElementById('form-resultado').addEventListener('submit', salvarResultado);
    document.getElementById('form-tabela').addEventListener('submit', salvarTabelaPontos);

    // Botões de Navegação (Voltar)
    document.getElementById('btn-voltar-home').addEventListener('click', voltarParaHome);
    document.getElementById('btn-voltar-categorias').addEventListener('click', voltarParaCategorias);
    document.getElementById('btn-cancelar-edicao').addEventListener('click', cancelarEdicao);
    document.getElementById('btn-voltar-baterias').addEventListener('click', abrirTelaBaterias);

    // Voltar para o Dashboard do Campeonato (usado em várias telas)
    const voltarParaPainel = () => {
        esconderTodasAsTelas();
        document.getElementById('tela-categorias').classList.remove('oculta');
    };
    document.getElementById('btn-voltar-campeonato-dash').addEventListener('click', voltarParaPainel);
    document.getElementById('btn-voltar-dash-classificacao').addEventListener('click', voltarParaPainel);

    // Botões de Ação Principal (Os Botões Coloridos)
    document.getElementById('btn-ir-baterias').addEventListener('click', abrirTelaBaterias);
    document.getElementById('btn-calcular-pontos').addEventListener('click', calcularPontosBateria);

    // A MÁGICA DO PÓDIO ESTÁ CONECTADA AQUI:
    document.getElementById('btn-gerar-pdf').addEventListener('click', abrirTelaClassificacao);
    document.getElementById('btn-imprimir-pdf').addEventListener('click', () => window.print());
});

function esconderTodasAsTelas() {
    document.getElementById('tela-home').classList.add('oculta');
    document.getElementById('tela-categorias').classList.add('oculta');
    document.getElementById('tela-pilotos').classList.add('oculta');
    document.getElementById('tela-baterias').classList.add('oculta');
    document.getElementById('tela-resultados').classList.add('oculta');
    document.getElementById('tela-classificacao').classList.add('oculta');
}

// ==========================================
// TELA 1 e 2: CAMPEONATOS E CATEGORIAS
// ==========================================
async function carregarCampeonatos() {
    const tabela = document.getElementById('tabela-campeonatos-corpo');
    try {
        const resposta = await fetch('http://localhost:8080/api/campeonatos');
        const campeonatos = await resposta.json();
        tabela.innerHTML = '';
        if (campeonatos.length === 0) { tabela.innerHTML = `<tr><td colspan="3" class="carregando">Nenhum campeonato criado.</td></tr>`; return; }
        campeonatos.forEach(camp => {
            tabela.innerHTML += `<tr><td>#${camp.id}</td><td><strong>${camp.nome}</strong></td><td><button class="btn btn-primario" onclick="abrirCampeonato(${camp.id}, '${camp.nome}')">Gerenciar ➔</button></td></tr>`;
        });
    } catch (erro) { console.error(erro); }
}

async function salvarCampeonato(event) {
    event.preventDefault();
    const nome = document.getElementById('nome-campeonato').value;
    try {
        await fetch('http://localhost:8080/api/campeonatos', { method: 'POST', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify({ nome: nome }) });
        document.getElementById('nome-campeonato').value = '';
        carregarCampeonatos();
    } catch (erro) { alert('Erro!'); }
}

function abrirCampeonato(id, nome) {
    campeonatoAtivoId = id;
    document.getElementById('titulo-campeonato-ativo').innerText = "🏁 " + nome;
    esconderTodasAsTelas();
    document.getElementById('tela-categorias').classList.remove('oculta');
    carregarCategorias();
}

function voltarParaHome() {
    campeonatoAtivoId = null;
    esconderTodasAsTelas();
    document.getElementById('tela-home').classList.remove('oculta');
}

async function carregarCategorias() {
    const tabela = document.getElementById('tabela-categorias-corpo');
    try {
        const resposta = await fetch(`http://localhost:8080/api/categorias/campeonato/${campeonatoAtivoId}`);
        const categorias = await resposta.json();
        tabela.innerHTML = '';
        if (categorias.length === 0) { tabela.innerHTML = `<tr><td colspan="3" class="carregando">Nenhuma categoria neste campeonato.</td></tr>`; return; }
        categorias.forEach(cat => {
            tabela.innerHTML += `<tr><td>#${cat.id}</td><td><strong>${cat.nome}</strong></td><td><button class="btn btn-primario" onclick="abrirCategoria(${cat.id}, '${cat.nome}')">Ver Pilotos ➔</button></td></tr>`;
        });
    } catch (erro) { console.error(erro); }
}

async function salvarCategoria(event) {
    event.preventDefault();
    const nome = document.getElementById('nome-categoria').value;
    try {
        await fetch('http://localhost:8080/api/categorias', { method: 'POST', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify({ nome: nome, campeonato: { id: campeonatoAtivoId } }) });
        document.getElementById('nome-categoria').value = '';
        carregarCategorias();
    } catch (erro) { alert('Erro!'); }
}

// ==========================================
// TELA 3: PILOTOS
// ==========================================
function abrirCategoria(id, nome) {
    categoriaAtivaId = id;
    document.getElementById('titulo-categoria-ativa').innerText = "🏎️ Categoria: " + nome;
    esconderTodasAsTelas();
    document.getElementById('tela-pilotos').classList.remove('oculta');
    carregarPilotos();
}

function voltarParaCategorias() {
    categoriaAtivaId = null;
    cancelarEdicao();
    esconderTodasAsTelas();
    document.getElementById('tela-categorias').classList.remove('oculta');
}

async function carregarPilotos() {
    const tabela = document.getElementById('tabela-corpo');
    try {
        const resposta = await fetch('http://localhost:8080/api/pilotos');
        const todosPilotos = await resposta.json();
        const pilotosDestaCategoria = todosPilotos.filter(p => p.categoria && p.categoria.id === categoriaAtivaId);
        tabela.innerHTML = '';
        if (pilotosDestaCategoria.length === 0) { tabela.innerHTML = `<tr><td colspan="4" class="carregando">Nenhum piloto nesta categoria.</td></tr>`; return; }
        pilotosDestaCategoria.forEach(piloto => {
            tabela.innerHTML += `<tr><td>#${piloto.id}</td><td>🏎️ <strong>${piloto.numeroKart}</strong></td><td>${piloto.nome}</td>
                <td><button class="btn" style="background-color: #f39c12; color: white;" onclick="prepararEdicao(${piloto.id}, '${piloto.nome}', ${piloto.numeroKart})">✏️ Editar</button></td></tr>`;
        });
    } catch (erro) { console.error(erro); }
}

async function salvarPiloto(event) {
    event.preventDefault();
    const idPilotoEdicao = document.getElementById('edit-piloto-id').value;
    const nome = document.getElementById('nome-piloto').value;
    const numero = document.getElementById('numero-piloto').value;
    const dadosPiloto = { nome: nome, numeroKart: parseInt(numero), categoria: { id: categoriaAtivaId } };

    try {
        if (idPilotoEdicao) {
            await fetch(`http://localhost:8080/api/pilotos/${idPilotoEdicao}`, { method: 'PUT', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify(dadosPiloto) });
        } else {
            await fetch('http://localhost:8080/api/pilotos', { method: 'POST', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify(dadosPiloto) });
        }
        cancelarEdicao(); carregarPilotos();
    } catch (erro) { alert('Erro!'); }
}

function prepararEdicao(id, nome, numeroKart) {
    document.getElementById('edit-piloto-id').value = id; document.getElementById('nome-piloto').value = nome; document.getElementById('numero-piloto').value = numeroKart;
    document.getElementById('titulo-form-piloto').innerText = "✏️ Editando Piloto"; document.getElementById('btn-salvar-piloto').innerText = "Atualizar"; document.getElementById('btn-cancelar-edicao').classList.remove('oculta');
}

function cancelarEdicao() {
    document.getElementById('edit-piloto-id').value = ''; document.getElementById('nome-piloto').value = ''; document.getElementById('numero-piloto').value = '';
    document.getElementById('titulo-form-piloto').innerText = "Cadastrar Novo Piloto"; document.getElementById('btn-salvar-piloto').innerText = "Salvar Piloto"; document.getElementById('btn-cancelar-edicao').classList.add('oculta');
}

// ==========================================
// REGRAS DE PONTUAÇÃO (Tabelas)
// ==========================================
async function salvarTabelaPontos(event) {
    event.preventDefault();
    const nome = document.getElementById('nome-tabela').value;
    const valoresTexto = document.getElementById('valores-tabela').value;
    const arrayValores = valoresTexto.split(',');
    const mapaPontos = {};
    arrayValores.forEach((valorString, index) => { mapaPontos[index + 1] = parseInt(valorString.trim()); });

    try {
        await fetch('http://localhost:8080/api/tabelas', { method: 'POST', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify({ nome: nome, pontosPorPosicao: mapaPontos }) });
        document.getElementById('nome-tabela').value = ''; document.getElementById('valores-tabela').value = '';
        carregarOpcoesTabelas(); alert('Regra salva!');
    } catch (erro) { alert('Erro ao salvar tabela!'); }
}

async function carregarOpcoesTabelas() {
    const select = document.getElementById('select-tabela-pontos');
    try {
        const resposta = await fetch('http://localhost:8080/api/tabelas');
        const tabelas = await resposta.json();
        if(select) { // Verifica se a tela já existe no HTML
            select.innerHTML = '<option value="">Qual regra usar?</option>';
            tabelas.forEach(t => { select.innerHTML += `<option value="${t.id}">${t.nome}</option>`; });
        }
    } catch (erro) { console.error(erro); }
}

// ==========================================
// TELA 4 e 5: BATERIAS E RESULTADOS
// ==========================================
function abrirTelaBaterias() {
    esconderTodasAsTelas();
    document.getElementById('tela-baterias').classList.remove('oculta');
    carregarBaterias();
}

async function carregarBaterias() {
    const tabela = document.getElementById('tabela-baterias-corpo');
    try {
        const resposta = await fetch(`http://localhost:8080/api/baterias/campeonato/${campeonatoAtivoId}`);
        const baterias = await resposta.json();
        tabela.innerHTML = '';
        if (baterias.length === 0) { tabela.innerHTML = `<tr><td colspan="3" class="carregando">Nenhuma corrida registrada.</td></tr>`; return; }
        baterias.forEach(bat => {
            tabela.innerHTML += `<tr><td>#${bat.id}</td><td><strong>${bat.nome}</strong></td>
                <td><button class="btn btn-primario" onclick="abrirResultadosBateria(${bat.id}, '${bat.nome}')">Lançar Resultados ➔</button></td></tr>`;
        });
    } catch (erro) { console.error(erro); }
}

async function salvarBateria(event) {
    event.preventDefault();
    const nome = document.getElementById('nome-bateria').value;
    try {
        await fetch('http://localhost:8080/api/baterias', { method: 'POST', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify({ nome: nome, campeonato: { id: campeonatoAtivoId } }) });
        document.getElementById('nome-bateria').value = ''; carregarBaterias();
    } catch (erro) { alert('Erro!'); }
}

function abrirResultadosBateria(id, nome) {
    bateriaAtivaId = id;
    document.getElementById('titulo-bateria-ativa').innerText = "🏁 " + nome;
    esconderTodasAsTelas();
    document.getElementById('tela-resultados').classList.remove('oculta');
    carregarOpcoesPilotos(); carregarResultados();
}

async function carregarOpcoesPilotos() {
    const select = document.getElementById('select-piloto-resultado');
    try {
        const respCat = await fetch(`http://localhost:8080/api/categorias/campeonato/${campeonatoAtivoId}`);
        const categorias = await respCat.json();
        const idsCategorias = categorias.map(c => c.id);

        const respPil = await fetch('http://localhost:8080/api/pilotos');
        const todosPilotos = await respPil.json();
        const pilotosDoCamp = todosPilotos.filter(p => p.categoria && idsCategorias.includes(p.categoria.id));

        select.innerHTML = '<option value="">Selecione o piloto...</option>';
        pilotosDoCamp.forEach(p => { select.innerHTML += `<option value="${p.id}">Kart #${p.numeroKart} - ${p.nome} (${p.categoria.nome})</option>`; });
    } catch (erro) { console.error(erro); }
}

async function salvarResultado(event) {
    event.preventDefault();
    const pilotoId = document.getElementById('select-piloto-resultado').value;
    const posicao = document.getElementById('posicao-chegada').value;
    const pacote = { bateria: { id: bateriaAtivaId }, piloto: { id: parseInt(pilotoId) }, posicaoChegada: parseInt(posicao) };

    try {
        await fetch('http://localhost:8080/api/resultados', { method: 'POST', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify(pacote) });
        document.getElementById('posicao-chegada').value = ''; carregarResultados();
    } catch (erro) { alert('Erro!'); }
}

async function carregarResultados() {
    const tabela = document.getElementById('tabela-resultados-corpo');
    try {
        const resposta = await fetch('http://localhost:8080/api/resultados');
        const todosResultados = await resposta.json();
        const resultadosDestaBateria = todosResultados.filter(r => r.bateria && r.bateria.id === bateriaAtivaId);
        resultadosDestaBateria.sort((a, b) => a.posicaoChegada - b.posicaoChegada);

        tabela.innerHTML = '';
        if (resultadosDestaBateria.length === 0) { tabela.innerHTML = `<tr><td colspan="5" class="carregando">Nenhum piloto cruzou a linha de chegada.</td></tr>`; return; }

        resultadosDestaBateria.forEach(res => {
            const pontos = res.pontos != null ? res.pontos : '-';
            const pilotoNome = res.piloto ? res.piloto.nome : 'Desconhecido';
            const kart = res.piloto ? res.piloto.numeroKart : 'N/A';
            const cat = res.piloto && res.piloto.categoria ? res.piloto.categoria.nome : 'N/A';

            tabela.innerHTML += `<tr><td><strong>${res.posicaoChegada}º</strong></td><td>🏎️ ${kart}</td><td>${pilotoNome}</td>
                <td><span style="font-size: 0.85em; background: #eee; padding: 2px 6px; border-radius: 4px;">${cat}</span></td>
                <td><strong>${pontos} pts</strong></td></tr>`;
        });
    } catch (erro) { console.error(erro); }
}

async function calcularPontosBateria() {
    const tabelaId = document.getElementById('select-tabela-pontos').value;
    if (!tabelaId) { alert("Por favor, selecione uma Regra de Pontuação!"); return; }
    try {
        const resposta = await fetch(`http://localhost:8080/api/resultados/calcular/${bateriaAtivaId}?tabelaId=${tabelaId}`, { method: 'POST' });
        if (resposta.ok) { alert('🏁 Sucesso!'); carregarResultados(); } else { alert('Erro!'); }
    } catch (erro) { console.error(erro); }
}

// ==========================================
// TELA 6: CLASSIFICAÇÃO FINAL (PÓDIO)
// ==========================================
// Variável local para guardar a lista atual da tela e permitir mover
let dadosClassificacaoLocal = [];

async function abrirTelaClassificacao() {
    esconderTodasAsTelas();
    document.getElementById('tela-classificacao').classList.remove('oculta');

    const resposta = await fetch(`http://localhost:8080/api/campeonatos/${campeonatoAtivoId}/classificacao`);
    dadosClassificacaoLocal = await resposta.json();

    renderizarTabelaClassificacao();
}

function renderizarTabelaClassificacao() {
    const tabela = document.getElementById('tabela-classificacao-corpo');
    tabela.innerHTML = '';

    dadosClassificacaoLocal.forEach((linha, index) => {
        const cat = linha.piloto.categoria ? linha.piloto.categoria.nome : '-';
        const posicao = index + 1;
        const medalha = posicao === 1 ? '🥇' : (posicao === 2 ? '🥈' : (posicao === 3 ? '🥉' : posicao + 'º'));

        tabela.innerHTML += `
            <tr>
                <td><strong style="font-size: 1.2em;">${medalha}</strong></td>
                <td>🏎️ ${linha.piloto.numeroKart}</td>
                <td><strong>${linha.piloto.nome}</strong></td>
                <td>${cat}</td>
                <td style="color: #27ae60; font-weight: bold;">${linha.totalPontos} pts</td>
                <td class="no-print">
                    <button class="btn" style="padding: 2px 5px; background: #eee;" onclick="moverPosicao(${index}, -1)">🔼</button>
                    <button class="btn" style="padding: 2px 5px; background: #eee;" onclick="moverPosicao(${index}, 1)">🔽</button>
                </td>
            </tr>`;
    });
}
function moverPosicao(index, direcao) {
    const novaPosicao = index + direcao;
    if (novaPosicao < 0 || novaPosicao >= dadosClassificacaoLocal.length) return;

    // Troca os pilotos de lugar no array
    const temp = dadosClassificacaoLocal[index];
    dadosClassificacaoLocal[index] = dadosClassificacaoLocal[novaPosicao];
    dadosClassificacaoLocal[novaPosicao] = temp;

    renderizarTabelaClassificacao();
}

