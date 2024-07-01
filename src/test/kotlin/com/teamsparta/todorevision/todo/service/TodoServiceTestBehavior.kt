package com.teamsparta.todorevision.todo.service

import com.teamsparta.todorevision.domain.like.repository.LikeRepository
import com.teamsparta.todorevision.domain.member.model.Member
import com.teamsparta.todorevision.domain.member.model.Profile
import com.teamsparta.todorevision.domain.member.repository.MemberRepository
import com.teamsparta.todorevision.domain.todo.dto.request.TodoCreateRequest
import com.teamsparta.todorevision.domain.todo.repository.TodoRepository
import com.teamsparta.todorevision.domain.todo.service.TodoService
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.shouldBe
import io.mockk.clearAllMocks
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.extension.ExtendWith
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
@ExtendWith(MockKExtension::class)
class TodoServiceTestBehavior @Autowired constructor(
    private val todoRepository: TodoRepository,
    private val memberRepository: MemberRepository,
    private val likeRepository: LikeRepository,
) : BehaviorSpec(
    {
        extension(SpringExtension)

        afterContainer { clearAllMocks() }

//        val todoRepository = mockk<TodoRepository>()
//        val memberRepository = mockk<MemberRepository>()
//        val likeRepository = mockk<LikeRepository>()

        val todoService = TodoService(todoRepository, memberRepository, likeRepository)
        val log = LoggerFactory.getLogger("내가 만든 쿠기")
        Given("게시글을 생성할 때") {
            When("멤버가 존재하면") {
                then("게시글이 생성된다.") {


//                    every { memberRepository.findByIdOrNull(any()) } returns Member(
//                        email = "test1@test.com",
//                        password = "test1",
//                        profile = Profile(nickname = "test1"),
//                        id = 1L
//                    )
//
//                    every { todoRepository.save(any()) } returns Todo(
//                        title = "테스트 제목",
//                        content = "테스트 내용",
//                        member = Member(
//                            email = "test1@test.com",
//                            password = "test1",
//                            profile = Profile(nickname = "test1"),
//                            id = 1L
//                        ),
//                        id = 1L
//                    )

//                    log.info("1번")
                        memberRepository.save(
                        Member(
                            email = "test1@test.com",
                            password = "test1",
                            profile = Profile(nickname = "test1")
                        )
                    )

//                    log.info(member.toString())

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
            }
        }
    }
) {

}