package com.tbfp.teamplannerbe.domain.comment.controller;


import com.tbfp.teamplannerbe.domain.comment.dto.CommentRequestDto;
import com.tbfp.teamplannerbe.domain.comment.dto.CommentRequestDto.CreateCommentRequestDto;
import com.tbfp.teamplannerbe.domain.comment.dto.CommentRequestDto.CommentToCommentCreateRequestDto;
import com.tbfp.teamplannerbe.domain.comment.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping("/api/v1/board/{boardId}/comment")
@Tag(name= "CommentController", description = "댓글 API")
public class CommentController {

    private final CommentService commentService;

    @Operation(summary = "공고에 대한 대댓글 조회", description = "boardId 와 commentId 를 통해 조회를 한다")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "500", description = "내부 서버 에러"),
    })
    @GetMapping("/{commentId}")
    public ResponseEntity<?> getCommentToCommentList(@PathVariable Long boardId,@PathVariable Long commentId){

        return ResponseEntity.status(HttpStatus.OK).body(commentService.getCommentToCommentList(boardId,commentId));
    }




    @Operation(summary = "공고에 대한 댓글작성", description = "userId와 boardId를 통해서 댓글을 작성한다")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "500", description = "내부 서버 에러"),
    })
    @PostMapping("")
    public ResponseEntity<?> createComment(@RequestBody CreateCommentRequestDto createCommentRequestDto){

        return ResponseEntity.status(HttpStatus.OK).body(commentService.sendComment(createCommentRequestDto));
    }

    @Operation(summary = "공고에 대한 대댓글작성", description = "userId와 boardId를 통해서 대댓글을 작성한다")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "500", description = "내부 서버 에러"),
    })
    @PostMapping("/{commentId}/comment")
    public ResponseEntity<?> createCommentToComment(@RequestBody CommentToCommentCreateRequestDto commentToCommentCreateRequestDto, Principal principal){

        return ResponseEntity.status(HttpStatus.OK).body(commentService.sendCommentToComment(commentToCommentCreateRequestDto,principal.getName()));
    }



    @DeleteMapping("/{commentId}")
    @Operation(summary = "댓글삭제 API", description = "공고아이디와 댓글아이디로 댓글 삭제")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "500", description = "내부 서버 에러"),
    })
    public ResponseEntity<String> deleteComment(@PathVariable Long boardId, @PathVariable Long commentId){
        commentService.deleteComment(boardId,commentId);
        return ResponseEntity.status(HttpStatus.OK).body("정상적으로 삭제되었습니다");
    }


    @PutMapping("")
    @Operation(summary = "댓글수정 API", description = "공고아이디와 댓글아이디로 댓글 수정")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "500", description = "내부 서버 에러"),
    })
    public ResponseEntity<?> updateComment(@RequestBody CommentRequestDto.UpdateCommentRequestDto updateCommentRequestDto){
        return ResponseEntity.status(HttpStatus.OK).body(commentService.updateComment(updateCommentRequestDto));
    }


}
