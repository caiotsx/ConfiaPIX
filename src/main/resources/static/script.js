const fileInput = document.getElementById('fileInput');
const selectFileBtn = document.getElementById('selectFileBtn');
const preview = document.getElementById('preview');
const imagePreview = document.getElementById('imagePreview');
const pdfPreview = document.getElementById('pdfPreview');
const fileName = document.getElementById('fileName');
const removeBtn = document.getElementById('removeBtn');
const validateBtn = document.getElementById('validateBtn');
const statusBox = document.getElementById('status');

selectFileBtn.addEventListener('click', () => fileInput.click());

fileInput.addEventListener('change', function () {
    const file = this.files[0];
    if (!file) return;
    const fileURL = URL.createObjectURL(file);
    preview.style.display = 'block';
    fileName.textContent = file.name;

    if (file.type === 'application/pdf') {
        pdfPreview.src = fileURL;
        pdfPreview.style.display = 'block';
        imagePreview.style.display = 'none';
    } else if (file.type.startsWith('image/')) {
        imagePreview.src = fileURL;
        imagePreview.style.display = 'block';
        pdfPreview.style.display = 'none';
    }
});

removeBtn.addEventListener('click', () => {
    fileInput.value = '';
    preview.style.display = 'none';
    fileName.textContent = '';
    imagePreview.src = '';
    pdfPreview.src = '';
    statusBox.style.display = 'none';
});

validateBtn.addEventListener('click', async () => {
    const file = fileInput.files[0];
    if (!file) {
        alert("Por favor, selecione um comprovante antes de validar.");
        return;
    }

    statusBox.style.display = 'block';
    statusBox.style.background = '#fff8e1';
    statusBox.style.color = '#444';
    statusBox.textContent = 'Validando comprovante...';

    const formData = new FormData();
    formData.append('comprovante', file);

    try {
        // 🔗 Substitua pela URL real da sua API:
        const response = await fetch('https://suaapi.com/api/validar-comprovante', {
            method: 'POST',
            body: formData
        });

        const result = await response.json();

        if (result.status === 'válido') {
            statusBox.style.background = '#e0f2f1';
            statusBox.style.color = '#00796b';
            statusBox.textContent = `✅ ${result.mensagem}`;
        } else if (result.status === 'falso') {
            statusBox.style.background = '#ffebee';
            statusBox.style.color = '#c62828';
            statusBox.textContent = `❌ ${result.mensagem}`;
        } else {
            statusBox.style.background = '#fff3e0';
            statusBox.style.color = '#ef6c00';
            statusBox.textContent = `⚠️ ${result.mensagem}`;
        }

    } catch (error) {
        statusBox.style.background = '#ffebee';
        statusBox.style.color = '#c62828';
        statusBox.textContent = 'Erro ao validar o comprovante. Tente novamente.';
    }
});