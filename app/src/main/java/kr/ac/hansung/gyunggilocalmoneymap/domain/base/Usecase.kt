package kr.ac.hansung.gyunggilocalmoneymap.domain.base

interface Usecase<Type, in Params> {

    operator fun invoke(params: Params): Type
}