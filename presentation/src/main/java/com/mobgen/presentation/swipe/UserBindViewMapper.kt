package com.mobgen.presentation.swipe

import com.mobgen.domain.model.User
import com.mobgen.presentation.ViewMapper
import javax.inject.Inject

class UserBindViewMapper @Inject constructor() : ViewMapper<User, SwipeViewModel.UserBindView> {
    override fun map(value: User): SwipeViewModel.UserBindView {
        return SwipeViewModel.UserBindView(
            value.id,
            value.name,
            value.description,
            value.birthDay,
            value.photos
        )
    }
}