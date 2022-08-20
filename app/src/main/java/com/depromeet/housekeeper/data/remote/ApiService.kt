package com.depromeet.housekeeper.data.remote


import com.depromeet.housekeeper.model.*
import com.depromeet.housekeeper.model.HouseWorkCreateResponse
import com.depromeet.housekeeper.model.LoginResponse
import retrofit2.http.*

interface ApiService {
    /**
    fcm
     */
    @POST("/api/fcm/token")
    suspend fun saveToken(@Body token: Token)

    @POST("/api/fcm/message")
    suspend fun sendMessage(@Body message: Message): Message

    /**
     * houseWorks
     */
    @POST("/api/houseworks")
    suspend fun createHouseWorks(@Body houseWorks: Chores): HouseWorkCreateResponse

    @PUT("/api/houseworks/{houseWorkId}")
    suspend fun editHouseWork(@Path("houseWorkId") houseWorkId: Int, @Body chore: Chore): HouseWork

    @DELETE("/api/houseworks/{houseWorkId}")
    suspend fun deleteHouseWork(@Path("houseWorkId") houseWorkId: Int)

    @PATCH("/api/houseworks/{houseWorkId}")
    suspend fun updateChoreState(
        @Path("houseWorkId") houseWorkId: Int,
        @Body updateChoreBody: UpdateChoreBody,
    ): UpdateChoreResponse

    @GET("/api/houseworks/{houseWorkId}/detail")
    suspend fun getDetailHouseWork(@Path("houseWorkId") houseWorkId: Int): HouseWork

    @GET("/api/houseworks/list")
    suspend fun getDateHouseWorkList(
        @Query("fromDate") fromDate: String,
        @Query("toDate") toDate: String
    ): Map<String, HouseWorks>

    @GET("/api/houseworks/list/member/{teamMemberId}")
    suspend fun getPeriodHouseWorkListOfMember(
        @Path("teamMemberId") teamMemberId: Int,
        @Query("fromDate") fromDate: String,
        @Query("toDate") toDate: String
    ): Map<String, HouseWorks> // ex) key: 2022-08-14

    @GET("/api/houseworks/success/count")
    suspend fun getCompletedHouseWorkNumber(@Query("scheduledDate") scheduledDate: String): CompleteHouseWork

    // todo 삭제 예정
    @GET("/api/houseworks")
    suspend fun getList(@Query("scheduledDate") scheduledDate: String): List<HouseWorks>

    /**
     * members
     */
    @GET("/api/member/profile-image")
    suspend fun getProfileImages(): ProfileImages

    @PATCH("/api/member")
    suspend fun updateMember(@Body updateMember: UpdateMember): UpdateMemberResponse

    @GET("/api/member/me")
    suspend fun getMe(): ProfileData

    @PATCH("/api/member")
    suspend fun updateMe(@Body editProfileModel: EditProfileModel): EditResponseBody


    /**
     * oauth
     */
    @POST("/api/oauth/login")
    suspend fun googlelogin(@Body socialType: SocialType): LoginResponse

    @POST("/api/oauth/logout")
    suspend fun logout()

    /**
     * presets
     */
    @GET("/api/preset")
    suspend fun getChoreList(): List<ChoreList>

    /**
     * rules
     */
    @POST("/api/rules")
    suspend fun createRules(@Body rule: Rule): RuleResponses

    @GET("/api/rules")
    suspend fun getRules(): RuleResponses

    @DELETE("/api/rules/{ruleId}")
    suspend fun deleteRule(@Path("ruleId") ruleId: Int): Response

    /**
     * teams
     */
    @POST("/api/teams")
    suspend fun buildTeam(@Body buildTeam: BuildTeam): BuildTeamResponse

    @GET("/api/teams/my")
    suspend fun getTeamData(): Groups

    @GET("/api/teams/invite-codes")
    suspend fun getInviteCode(): GetInviteCode

    @PATCH("/api/teams")
    suspend fun updateTeam(@Body teamName: BuildTeam): TeamUpdateResponse

    @POST("/api/teams/join")
    suspend fun joinTeam(@Body inviteCode: JoinTeam): JoinTeamResponse

    @POST("/api/teams/leave")
    suspend fun leaveTeam()

}