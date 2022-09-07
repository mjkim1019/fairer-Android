package com.depromeet.housekeeper.data.repository

import com.depromeet.housekeeper.data.RetrofitBuilder
import com.depromeet.housekeeper.data.remote.RemoteDataSource
import com.depromeet.housekeeper.model.HouseWork
import com.depromeet.housekeeper.model.request.UpdateChoreBody
import com.depromeet.housekeeper.model.request.UpdateMember
import com.depromeet.housekeeper.model.request.*
import com.depromeet.housekeeper.model.response.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

object Repository : RemoteDataSource {
    private val apiService = RetrofitBuilder.apiService

    override suspend fun createHouseWorks(houseWorks: Chores): Flow<HouseWorkCreateResponse> =
        flow {
            emit(apiService.createHouseWorks(houseWorks))
        }

    override suspend fun getList(scheduledDate: String): Flow<List<HouseWorks>> = flow {
        emit(apiService.getList(scheduledDate))
    }

    override suspend fun getHouseWorkList(): Flow<List<ChoreList>> = flow {
        emit(apiService.getChoreList())
    }

    override suspend fun getCompletedHouseWorkNumber(scheduledDate: String): Flow<CompleteHouseWork> =
        flow {
            emit(apiService.getCompletedHouseWorkNumber(scheduledDate))
        }

    override suspend fun getGoogleLogin(
        socialType: SocialType,
    ): Flow<LoginResponse> = flow {
        emit(apiService.googlelogin(socialType))
    }

    override suspend fun deleteHouseWork(id: Int): Flow<Unit> =
        flow {
            emit(apiService.deleteHouseWork(id))
        }

    override suspend fun editHouseWork(id: Int, chore: Chore): Flow<HouseWork> = flow {
        apiService.editHouseWork(id, chore)
    }

    override suspend fun updateChoreState(
        houseWorkId: Int,
        updateChoreBody: UpdateChoreBody,
    ): Flow<UpdateChoreResponse> =
        flow {
            emit(apiService.updateChoreState(houseWorkId, updateChoreBody))
        }

    override suspend fun logout(
    ): Flow<Unit> = flow {
        emit(apiService.logout())
    }

    override suspend fun buildTeam(
        buildTeam: BuildTeam,
    ): Flow<BuildTeamResponse> = flow {
        emit(apiService.buildTeam(buildTeam))
    }

    override suspend fun getTeam(): Flow<Groups> = flow {
        emit(apiService.getTeamData())
    }

    override suspend fun getProfileImages(): Flow<ProfileImages> = flow {
        emit(apiService.getProfileImages())
    }

    override suspend fun updateMember(updateMember: UpdateMember): Flow<UpdateMemberResponse> =
        flow {
            emit(apiService.updateMember(updateMember = updateMember))
        }

    override suspend fun getInviteCode(): Flow<GetInviteCode> = flow {
        emit(apiService.getInviteCode())
    }

    override suspend fun updateTeam(teamName: BuildTeam): Flow<TeamUpdateResponse> = flow {
        emit(apiService.updateTeam(teamName))
    }

    override suspend fun joinTeam(inviteCode: JoinTeam): Flow<JoinTeamResponse> = flow {
        emit(apiService.joinTeam(inviteCode))
    }


    override suspend fun createRule(rule: Rule): Flow<RuleResponses> = flow {
        emit(apiService.createRules(rule))
    }

    override suspend fun getRules(): Flow<RuleResponses> = flow {
        emit(apiService.getRules())
    }

    override suspend fun deleteRule(ruleId: Int): Flow<Response> = flow {
        emit(apiService.deleteRule(ruleId))
    }

    override suspend fun leaveTeam(): Flow<Unit> = flow {
        emit(apiService.leaveTeam())
    }

    override suspend fun getMe(): Flow<ProfileData> = flow {
        emit(apiService.getMe())
    }

    override suspend fun updateMe(editProfileModel: EditProfileModel): Flow<EditResponseBody> =
        flow {
            emit(apiService.updateMe(editProfileModel))
        }

    override suspend fun getDetailHouseWorks(houseWorkId: Int): Flow<HouseWork> = flow {
        emit(apiService.getDetailHouseWork(houseWorkId))
    }

    override suspend fun getDateHouseWorkList(
        fromDate: String,
        toDate: String
    ): Flow<Map<String, HouseWorks>> {
        return flow {
            emit(apiService.getDateHouseWorkList(fromDate, toDate))
        }
    }

    override suspend fun getPeriodHouseWorkListOfMember(
        teamMemberId: Int,
        fromDate: String,
        toDate: String
    ): Flow<Map<String, HouseWorks>> {
        return flow {
            emit(apiService.getPeriodHouseWorkListOfMember(teamMemberId, fromDate, toDate))
        }
    }

    override suspend fun saveToken(token: Token): Flow<Unit> = flow {
        emit(apiService.saveToken(token = token))
    }

    override suspend fun sendMessage(message: Message): Flow<Message> = flow {
        emit(apiService.sendMessage(message = message))
    }

}
