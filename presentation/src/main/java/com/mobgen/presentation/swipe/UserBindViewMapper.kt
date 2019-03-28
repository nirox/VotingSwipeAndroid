package com.mobgen.presentation.swipe

import com.mobgen.domain.model.User
import com.mobgen.presentation.ViewMapper

class UserBindViewMapper : ViewMapper<User, SwipeViewModel.UserBindView> {
    override fun map(value: User): SwipeViewModel.UserBindView {
        return SwipeViewModel.UserBindView(value.photos)
    }
}