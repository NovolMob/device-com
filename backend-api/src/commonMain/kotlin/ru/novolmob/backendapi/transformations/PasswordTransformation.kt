package ru.novolmob.backendapi.transformations

import ru.novolmob.core.models.Password

expect class PasswordTransformation: Hashing<Password>