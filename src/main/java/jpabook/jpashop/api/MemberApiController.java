package jpabook.jpashop.api;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.service.MemberService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class MemberApiController {
    private final MemberService memberService;

    @PostMapping("/api/v1/members")
    public CreateMemberResponse createMemberV1(@RequestBody @Valid Member member) {
        memberService.join(member);
        return new CreateMemberResponse(member);
    }

    @PostMapping("/api/v2/members")
    public CreateMemberResponse createMemberV2(@RequestBody @Valid CreateMemberRequest request) {
        Member member = new Member();
        member.setName(request.getName());
        memberService.join(member);
        return new CreateMemberResponse(member);
    }

    @Data
    static class CreateMemberResponse {
        private Long id;
        private String name;

        public CreateMemberResponse(Member member) {
            id = member.getId();
            name = member.getName();
        }
    }

    @Data
    static class CreateMemberRequest {
        @NotEmpty
        private String name;
    }

    @PutMapping("/api/v2/members/{id}")
    public UpdateMemberResponse updateMemberV2(@PathVariable("id") Long id, @RequestBody @Valid UpdateMemberRequest request) {
        memberService.update(id, request.getName());
        Member member = memberService.findOne(id);
        return new UpdateMemberResponse(member);
    }

    @Data
    static class UpdateMemberResponse {
        private Long id;
        private String name;

        UpdateMemberResponse(Member member) {
            id = member.getId();
            name = member.getName();
        }
    }

    @Data
    static class UpdateMemberRequest {
        @NotEmpty
        private String name;
    }

    @GetMapping("/api/v1/members")
    public List<Member> membersV1() {
        List<Member> members = memberService.findMembers();
        return members;
    }

    @GetMapping("/api/v2/members")
    public List<MemberDTO> membersV2() {
        List<Member> members = memberService.findMembers();
        List<MemberDTO> collect = members.stream().map(m -> new MemberDTO(m)).collect(Collectors.toList());
        return collect;
    }

    @Data
    static class MemberDTO {
        private Long id;
        private String name;

        public MemberDTO(Member member) {
            id = member.getId();
            name = member.getName();
        }
    }
}