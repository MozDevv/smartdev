package app.moz.smartdev.configs;

import lombok.Getter;

import java.util.List;

@Getter
public class ResponseBuilder {

    public static <T> ResponseWrapper<T> createResponseWrapper(List<T> data, int page, int size, boolean succeeded, String messages, int code) {
        int totalCount = data.size();
        int totalPages = (int) Math.ceil((double) totalCount / size);
        boolean hasPreviousPage = page > 1;
        boolean hasNextPage = page < totalPages;

        return ResponseWrapper.<T>builder()
                .data(data)
                .currentPage(page)
                .totalPages(totalPages)
                .totalCount(totalCount)
                .pageSize(size)
                .hasPreviousPage(hasPreviousPage)
                .hasNextPage(hasNextPage)
                .messages(messages)
                .succeeded(succeeded)
                .validationErrors(null)
                .exception(null)
                .code(code)
                .build();
    }

    public static <T> ResponseWrapper<T> createErrorResponse(String messages, String validationErrors, String exception, int code) {
        return ResponseWrapper.<T>builder()
                .data(null)
                .currentPage(0)
                .totalPages(0)
                .totalCount(0)
                .pageSize(0)
                .hasPreviousPage(false)
                .hasNextPage(false)
                .messages(messages)
                .succeeded(false)
                .validationErrors(validationErrors)
                .exception(exception)
                .code(code)
                .build();
    }
}