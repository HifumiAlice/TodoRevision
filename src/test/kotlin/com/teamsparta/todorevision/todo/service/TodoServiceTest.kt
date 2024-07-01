package com.teamsparta.todorevision.todo.service

import com.teamsparta.todorevision.domain.like.repository.LikeRepository
import com.teamsparta.todorevision.domain.member.model.Member
import com.teamsparta.todorevision.domain.member.model.Profile
import com.teamsparta.todorevision.domain.member.repository.MemberRepository
import com.teamsparta.todorevision.domain.todo.dto.request.TodoCreateRequest
import com.teamsparta.todorevision.domain.todo.model.Todo
import com.teamsparta.todorevision.domain.todo.repository.TodoRepository
import com.teamsparta.todorevision.domain.todo.service.TodoService
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.repository.findByIdOrNull

@SpringBootTest
class TodoServiceTest@Autowired constructor(
    private val todoRepository : TodoRepository,
    private val memberRepository : MemberRepository,
    private val likeRepository : LikeRepository
){
    private val todoService = TodoService(todoRepository, memberRepository, likeRepository)

    private val todoRepositoryMock = mockk<TodoRepository>()
    private val memberRepositoryMock = mockk<MemberRepository>()
    private val likeRepositoryMock = mockk<LikeRepository>()
    private val todoServiceMock = TodoService(todoRepositoryMock, memberRepositoryMock, likeRepositoryMock)

    @Test
    fun `게시글을 생성할 때 멤버가 존재하면 게시글을 생성한다`() {

        memberRepository.save(
            Member(
                email = "test1@test.com",
                password = "test1",
                profile = Profile(nickname = "test1")
            )
        )

        val result = todoService.createTodo(
            request = TodoCreateRequest(
                title = "테스트 제목",
                content = "테스트 내용"
            ), memberId = 1L
        )

        result.id shouldBe 1L
        result.member.memberId shouldBe 1L
        result.done shouldBe false
        result.title shouldBe "테스트 제목"
        result.content shouldBe "테스트 내용"
        result.liked shouldBe false
    }


    @Test
    fun `게시글을 생성할 때 멤버가 존재하지 않으면 예외를 발생한다`() {

        shouldThrow<RuntimeException> {  todoService.createTodo(
            request = TodoCreateRequest(
                title = "테스트 제목",
                content = "테스트 내용"
            ), memberId = 1L
        )}.let{
            it.message shouldBe "멤버가 존재하지 않습니다. 1"
        }

    }

    @Test
    fun `게시글을 생성할 때 멤버가 존재하면 게시글을 생성한다 Mock`() {

        every { memberRepositoryMock.findByIdOrNull(any()) } returns Member(
            email = "test1@test.com",
            password = "test1",
            profile = Profile(nickname = "test1"),
            id = 1L
        )

        every { todoRepositoryMock.save(any()) } returns Todo(
            title = "테스트 제목",
            content = "테스트 내용",
            member = Member(
                email = "test1@test.com",
                password = "test1",
                profile = Profile(nickname = "test1"),
                id = 1L
            ),
            id = 1L
        )

        val result = todoServiceMock.createTodo(
            request = TodoCreateRequest(
                title = "테스트 제목",
                content = "테스트 내용"
            ), memberId = 1L
        )

        result.id shouldBe 1L
        result.member.memberId shouldBe 1L
        result.done shouldBe false
        result.title shouldBe "테스트 제목"
        result.content shouldBe "테스트 내용"
        result.liked shouldBe false
    }

    @Test
    fun `게시글을 생성할 때 멤버가 존재하지 않으면 예외를 발생한다 Mock`() {

        every { memberRepositoryMock.findByIdOrNull(any()) } returns null


        shouldThrow<RuntimeException> {  todoServiceMock.createTodo(
            request = TodoCreateRequest(
                title = "테스트 제목",
                content = "테스트 내용"
            ), memberId = 1L
        )}.let{
            it.message shouldBe "멤버가 존재하지 않습니다. 1"
        }

    }
}