package kr.ac.hansung.localcurrency.data.remote.model

data class GeocodeResponse(
    val results: List<Result>,
    val status: Status
) {
    data class Result(
        val code: Code,
        val land: Land,
        val name: String,
        val region: Region
    ) {
        data class Code(
            val id: String,
            val mappingId: String,
            val type: String
        )

        data class Land(
            val addition0: Addition0,
            val addition1: Addition1,
            val addition2: Addition2,
            val addition3: Addition3,
            val addition4: Addition4,
            val coords: Coords,
            val name: String,
            val number1: String,
            val number2: String,
            val type: String
        ) {
            data class Addition0(
                val type: String,
                val value: String
            )

            data class Addition1(
                val type: String,
                val value: String
            )

            data class Addition2(
                val type: String,
                val value: String
            )

            data class Addition3(
                val type: String,
                val value: String
            )

            data class Addition4(
                val type: String,
                val value: String
            )

            data class Coords(
                val center: Center
            ) {
                data class Center(
                    val crs: String,
                    val x: Double,
                    val y: Double
                )
            }
        }

        data class Region(
            val area0: Area0,
            val area1: Area1,
            val area2: Area2,
            val area3: Area3,
            val area4: Area4
        ) {
            data class Area0(
                val coords: Coords,
                val name: String
            ) {
                data class Coords(
                    val center: Center
                ) {
                    data class Center(
                        val crs: String,
                        val x: Double,
                        val y: Double
                    )
                }
            }
            //시
            data class Area1(
                val alias: String,
                val coords: Coords,
                val name: String
            ) {
                data class Coords(
                    val center: Center
                ) {
                    data class Center(
                        val crs: String,
                        val x: Double,
                        val y: Double
                    )
                }
            }
            //군,구
            data class Area2(
                val coords: Coords,
                val name: String
            ) {
                data class Coords(
                    val center: Center
                ) {
                    data class Center(
                        val crs: String,
                        val x: Double,
                        val y: Double
                    )
                }
            }
            //읍,면,동
            data class Area3(
                val coords: Coords,
                val name: String
            ) {
                data class Coords(
                    val center: Center
                ) {
                    data class Center(
                        val crs: String,
                        val x: Double,
                        val y: Double
                    )
                }
            }

            data class Area4(
                val coords: Coords,
                val name: String
            ) {
                data class Coords(
                    val center: Center
                ) {
                    data class Center(
                        val crs: String,
                        val x: Double,
                        val y: Double
                    )
                }
            }
        }
    }

    data class Status(
        val code: Int,
        val message: String,
        val name: String
    )
}