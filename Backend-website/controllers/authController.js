const bcrypt = require('bcrypt');
const jwt = require('jsonwebtoken');
const users = require('../models/users');

// SECRET KEY untuk JWT
const SECRET_KEY = 'your_secret_key';

const registerUser = async (request, h) => {
    const { username, password } = request.payload;

    // Cek apakah username sudah terdaftar
    const userExists = users.find((user) => user.username === username);
    if (userExists) {
        return h.response({ message: 'Username sudah terdaftar' }).code(400);
    }

    // Hash password
    const hashedPassword = await bcrypt.hash(password, 10);

    // Tambahkan user ke "database"
    users.push({ username, password: hashedPassword });

    return h.response({ message: 'Registrasi berhasil' }).code(201);
};

const loginUser = async (request, h) => {
    const { username, password } = request.payload;

    // Cari user di "database"
    const user = users.find((u) => u.username === username);
    if (!user) {
        return h.response({ message: 'Username atau password salah' }).code(401);
    }

    // Validasi password
    const isPasswordValid = await bcrypt.compare(password, user.password);
    if (!isPasswordValid) {
        return h.response({ message: 'Username atau password salah' }).code(401);
    }

    // Buat token JWT
    const token = jwt.sign({ username }, SECRET_KEY, { expiresIn: '1h' });

    return h.response({ message: 'Login berhasil', token }).code(200);
};

module.exports = { registerUser, loginUser };
