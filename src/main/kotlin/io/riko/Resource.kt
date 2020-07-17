package io.riko

import org.jboss.resteasy.annotations.jaxrs.FormParam
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm
import org.jboss.resteasy.annotations.providers.multipart.PartType
import java.io.BufferedWriter
import java.io.File
import java.io.InputStream
import javax.ws.rs.*
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

private const val FILE_OUTPUT = "riko-pie"
private const val CONTENT_HEADER = "Ticker, Weight"

@Path("pie")
class RikoResource {

    @POST
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    fun generatePie(@MultipartForm multipart: MultipartBody): Response =
        when (multipart.file) {
            null -> Response.status(Response.Status.BAD_REQUEST).build()
            else -> processInput(multipart)
        }

    private fun processInput(multipart: MultipartBody): Response {
        val lines = multipart.file!!
            .bufferedReader()
            .readLines()

        val input = mapInput(multipart.strategy, lines)
        val file = prepareOutput(calculatePie(input))
        file.deleteOnExit()

        return Response.ok()
            .header("Content-Disposition", "attachment;filename=$FILE_OUTPUT-${multipart.strategy.name.toLowerCase()}.csv")
            .entity(file)
            .build()
    }

    private fun mapInput(strategy: Strategy, lines: List<String>): CalculationInput {
        // remove header if needed
        val items = when (lines[0].startsWith("ticker", ignoreCase = true)) {
            true -> lines.drop(1)
            false -> lines
        }

        val stocks = items.map { line ->
            val lineParts = line.split(",")
            Stock(
                ticker = lineParts[0].trim(),
                rating = lineParts[1].trim().toInt(),
                dividendYield = when (val value = lineParts[2].trim().toDouble()) {
                    0.0 -> 0.5
                    else -> value
                }
            )
        }.toSet()

        return CalculationInput(strategy, stocks)
    }

}


class MultipartBody {
    @FormParam("file")
    @PartType(MediaType.APPLICATION_OCTET_STREAM)
    var file: InputStream? = null

    @FormParam("strategy")
    @PartType(MediaType.TEXT_PLAIN)
    var strategy: Strategy = Strategy.RATING
}


/**
 * Write a file object containing the final formatted content
 */
private fun prepareOutput(slices: List<Slice>): File {
    val file = File(FILE_OUTPUT)

    file.bufferedWriter()
        .use { writer ->
            writeLine(writer, CONTENT_HEADER)
            slices.forEach { slice -> writeLine(writer, "${slice.ticker}, ${slice.weight}") }
        }

    return file
}

private fun writeLine(writer: BufferedWriter, content: String) {
    writer.write(content)
    writer.newLine()
}
