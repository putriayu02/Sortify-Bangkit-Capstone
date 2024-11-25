const Joi = require('joi');
const { registerUser, loginUser } = require('../controllers/authController');

const authRoutes = [
    {
        method: 'POST',
        path: '/register',
        handler: registerUser,
        options: {
            validate: {
                payload: Joi.object({
                    username: Joi.string().min(3).required(),
                    password: Joi.string().min(6).required(),
                }),
                failAction: (request, h, error) => {
                    return h.response({ message: error.details[0].message }).code(400).takeover();
                },
            },
        },
    },
    {
        method: 'POST',
        path: '/login',
        handler: loginUser,
        options: {
            validate: {
                payload: Joi.object({
                    username: Joi.string().required(),
                    password: Joi.string().required(),
                }),
                failAction: (request, h, error) => {
                    return h.response({ message: error.details[0].message }).code(400).takeover();
                },
            },
        },
    },
];

module.exports = authRoutes;
