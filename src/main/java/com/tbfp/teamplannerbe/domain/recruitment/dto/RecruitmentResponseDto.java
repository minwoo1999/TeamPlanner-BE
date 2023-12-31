package com.tbfp.teamplannerbe.domain.recruitment.dto;

import com.querydsl.core.annotations.QueryProjection;
import com.tbfp.teamplannerbe.domain.recruitment.entity.Recruitment;
import com.tbfp.teamplannerbe.domain.recruitmentApply.entity.RecruitmentApply;
import com.tbfp.teamplannerbe.domain.recruitmentApply.entity.RecruitmentApplyStateEnum;
import com.tbfp.teamplannerbe.domain.recruitmentComment.entity.RecruitmentComment;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RecruitmentResponseDto {

    @Getter
    @Builder
    @NoArgsConstructor
    public static class RecruitmentSearchDto {
        private Long id;
        private String title;
        private String content;
        private Integer viewCount;
        private Integer likeCount;
        private Integer currentMemberSize;
        private Integer maxMemberSize;
        private LocalDateTime createdAt;

        private String authorNickname;
        private String authorProfileImg;

        private Integer commentCount;
        private String recruitmentBoardRecruitmentPeriod;
        private String recruitmentBoardActivityName;
        private String recruitmentBoardImg;
        private String recruitmentBoardActivityField;
        private String recruitmentBoardCategory;

        @QueryProjection
        public RecruitmentSearchDto(Long id, String title, String content, Integer viewCount, Integer likeCount, Integer currentMemberSize, Integer maxMemberSize, LocalDateTime createdAt
                                    , String authorNickname
                                    , String authorProfileImg
                                    , Integer commentCount
                                    , String recruitmentBoardRecruitmentPeriod
                                    , String activityName
                                    , String recruitmentBoardImg
                                    , String recruitmentBoardActivityField
                                    , String recruitmentBoardCategory
        ) {
            this.id = id;
            this.title = title;
            this.content = content;
            this.viewCount = viewCount;
            this.likeCount = likeCount;
            this.currentMemberSize = currentMemberSize;
            this.maxMemberSize = maxMemberSize;
            this.createdAt = createdAt;
            this.authorNickname = authorNickname;
            this.authorProfileImg = authorProfileImg;
            this.commentCount = commentCount;
            this.recruitmentBoardRecruitmentPeriod = recruitmentBoardRecruitmentPeriod;
            this.recruitmentBoardActivityName = activityName;
            this.recruitmentBoardImg = recruitmentBoardImg;
            this.recruitmentBoardActivityField = recruitmentBoardActivityField;
            this.recruitmentBoardCategory = recruitmentBoardCategory;

//            this.recruitmentBoardRecruitmentEndDate = LocalDate.parse(recruitmentBoardRecruitmentPeriod.split("~")[1].trim(), DateTimeFormatter.ofPattern("yy.M.d")).atStartOfDay();
        }
    }
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RecruitmentCreateResponseDto {
        private Long id;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RecruitmentReadResponseDto {
        private Long id;
        private String title;
        private String content;
        private Integer maxMemberSize;
        private Integer currentMemberSize;
        private Integer viewCount;
        private Integer likeCount;
        private String type;
        public static RecruitmentReadResponseDto toDto(Recruitment recruitment) {
            return builder()
                    .id(recruitment.getId())
                    .title(recruitment.getTitle())
                    .content(recruitment.getContent())
                    .maxMemberSize(recruitment.getMaxMemberSize())
                    .currentMemberSize(recruitment.getCurrentMemberSize())
                    .viewCount(recruitment.getViewCount())
                    .likeCount(recruitment.getLikeCount())
                    .type(recruitment.getBoard().getCategory())
                    .build();
        }
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    public static class RecruitmentWithCommentResponseDto {
        private Long id;
        private String title;
        private String content;
        private Integer maxMemberSize;
        private Integer currentMemberSize;
        private Integer viewCount;
        private Integer likeCount;
        private String boardActivityName;
        private LocalDateTime boardEndDate;
        private String authorNickname;
        private String authorProfileImg;
        private Boolean recruitmentState;

        @Builder.Default
        private List<RecruitmentCommentDto> commentList = new ArrayList<>();

        public static RecruitmentWithCommentResponseDto toDto(boolean isAuthorOfRecruitment, String username, Recruitment recruitment) {
            return builder()
                    .id(recruitment.getId())
                    .title(recruitment.getTitle())
                    .content(recruitment.getContent())
                    .maxMemberSize(recruitment.getMaxMemberSize())
                    .currentMemberSize(recruitment.getCurrentMemberSize())
                    .viewCount(recruitment.getViewCount())
                    .likeCount(recruitment.getLikeCount())
                    .commentList(
                            recruitment.getCommentList().stream().map(c -> RecruitmentCommentDto.toDto(isAuthorOfRecruitment, username, c)).collect(Collectors.toList())
                    )
                    .boardActivityName(recruitment.getBoard().getActivityName())
                    .boardEndDate(LocalDate.parse(recruitment.getBoard().getRecruitmentPeriod().split("~")[1].trim(), DateTimeFormatter.ofPattern("yy.M.d")).atStartOfDay())
                    .authorNickname(recruitment.getAuthor().getNickname())
                    .authorProfileImg(recruitment.getAuthor().getBasicProfile().getProfileImage())
                    .recruitmentState(recruitment.getRecruitmentLikeList().stream().
                            map(like-> like.getMember().getUsername()).
                            anyMatch(userId -> userId.equals(username))
                    )

                    .build();
        }

        @Getter
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        private static class RecruitmentCommentDto {
            private Long id;
            private String content;
            private LocalDateTime createdAt;
            private Long parentCommentId;
            private String memberUsername;
            private String memberProfileImg;
            private String memberNickname;

            public static RecruitmentCommentDto toDto(boolean isAuthorOfRecruitment, String username, RecruitmentComment recruitmentComment) {
                String dtoUsername;
                String dtoContent;

                if (recruitmentComment.isDeleted()) {
                    dtoUsername = "알수없음";
                    dtoContent = "삭제된 댓글입니다";
                } else if (recruitmentComment.isConfidential() && !(
                        isAuthorOfRecruitment || recruitmentComment.getMember().getNickname().equals(username)
                        )) {
                    dtoUsername = "알수없음";
                    dtoContent = "익명 댓글입니다";
                } else {
                    dtoContent = recruitmentComment.getContent();
                    dtoUsername = recruitmentComment.getMember().getNickname();
                }
                return builder()
                        .id(recruitmentComment.getId())
                        .content(dtoContent)
                        .createdAt(recruitmentComment.getCreatedAt())
                        .memberUsername(dtoUsername)
                        .parentCommentId(recruitmentComment.getParentComment() == null ? null : recruitmentComment.getParentComment().getId())
                        .memberProfileImg(recruitmentComment.getMember().getBasicProfile().getProfileImage())
                        .memberNickname(recruitmentComment.getMember().getNickname())
                        .build();
            }
        }
    }

    @Getter
    @NoArgsConstructor
    @ToString
    public static class RecruitmentWithMemberWithApply{

        private RecruitmentApplyStateEnum state; // 승인여부상태
        private String userNickName; //유저아이디
        private String userProfile; //유저 프로필
        private String recruitmentTitle; // 모집글 제목
        private String content; // 지원할 때 쓰는 content
        private Long memberId;

        @Builder
        public RecruitmentWithMemberWithApply(RecruitmentApply apply) {
            this.state=apply.getState();
            this.userNickName = apply.getApplicant().getNickname();
            this.userProfile = apply.getApplicant().getBasicProfile().getProfileImage(); //프로필 없어서 userPhone 로함
            this.recruitmentTitle = apply.getRecruitment().getTitle();
            this.content = apply.getContent();
            this.memberId=apply.getApplicant().getId();
        }
    }


}
