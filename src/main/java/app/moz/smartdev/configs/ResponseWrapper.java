package app.moz.smartdev.configs;

import lombok.Data;

import java.util.List;

@Data
public class ResponseWrapper<T> {
    private List<T> data;
    private int currentPage;
    private int totalPages;
    private int totalCount;
    private int pageSize;
    private boolean hasPreviousPage;
    private boolean hasNextPage;
    private String messages;
    private boolean succeeded;
    private String validationErrors;
    private String exception;
    private int code;

    public static <T> Builder<T> builder() {
        return new Builder<>();
    }

    public static class Builder<T> {
        private List<T> data;
        private int currentPage;
        private int totalPages;
        private int totalCount;
        private int pageSize;
        private boolean hasPreviousPage;
        private boolean hasNextPage;
        private String messages;
        private boolean succeeded;
        private String validationErrors;
        private String exception;
        private int code;

        public Builder<T> data(List<T> data) {
            this.data = data;
            return this;
        }

        public Builder<T> currentPage(int currentPage) {
            this.currentPage = currentPage;
            return this;
        }

        public Builder<T> totalPages(int totalPages) {
            this.totalPages = totalPages;
            return this;
        }

        public Builder<T> totalCount(int totalCount) {
            this.totalCount = totalCount;
            return this;
        }

        public Builder<T> pageSize(int pageSize) {
            this.pageSize = pageSize;
            return this;
        }

        public Builder<T> hasPreviousPage(boolean hasPreviousPage) {
            this.hasPreviousPage = hasPreviousPage;
            return this;
        }

        public Builder<T> hasNextPage(boolean hasNextPage) {
            this.hasNextPage = hasNextPage;
            return this;
        }

        public Builder<T> messages(String messages) {
            this.messages = messages;
            return this;
        }

        public Builder<T> succeeded(boolean succeeded) {
            this.succeeded = succeeded;
            return this;
        }

        public Builder<T> validationErrors(String validationErrors) {
            this.validationErrors = validationErrors;
            return this;
        }

        public Builder<T> exception(String exception) {
            this.exception = exception;
            return this;
        }

        public Builder<T> code(int code) {
            this.code = code;
            return this;
        }

        public ResponseWrapper<T> build() {
            ResponseWrapper<T> responseWrapper = new ResponseWrapper<>();
            responseWrapper.data = this.data;
            responseWrapper.currentPage = this.currentPage;
            responseWrapper.totalPages = this.totalPages;
            responseWrapper.totalCount = this.totalCount;
            responseWrapper.pageSize = this.pageSize;
            responseWrapper.hasPreviousPage = this.hasPreviousPage;
            responseWrapper.hasNextPage = this.hasNextPage;
            responseWrapper.messages = this.messages;
            responseWrapper.succeeded = this.succeeded;
            responseWrapper.validationErrors = this.validationErrors;
            responseWrapper.exception = this.exception;
            responseWrapper.code = this.code;
            return responseWrapper;
        }
    }
}