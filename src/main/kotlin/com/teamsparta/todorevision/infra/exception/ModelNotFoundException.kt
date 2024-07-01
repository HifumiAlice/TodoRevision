package com.teamsparta.todorevision.infra.exception

class ModelNotFoundException(model: String, id: Long) : RuntimeException() {
    override var message: String = "Model $model not found with id $id"
}
