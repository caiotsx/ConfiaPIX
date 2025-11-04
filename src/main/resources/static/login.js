document.getElementById('loginForm').addEventListener('submit', (e) => {
      e.preventDefault();

      const username = document.getElementById('username').value;
      const password = document.getElementById('password').value;
      const photoFile = document.getElementById('photo').files[0];

      if (!username || !password || !photoFile) {
        alert('Por favor, preencha todos os campos.');
        return;
      }

      const reader = new FileReader();
      reader.onload = function(event) {
        const userData = {
          username: username,
          password: password,
          photo: event.target.result
        };
        localStorage.setItem('confiaPIX_user', JSON.stringify(userData));
        window.location.href = 'index.html';
      };
      reader.readAsDataURL(photoFile);
    });