package kr.ac.hansung.gyunggilocalmoneymap.domain.base

interface UseCase<Type, in Params> {

    operator fun invoke(params: Params): Type
}