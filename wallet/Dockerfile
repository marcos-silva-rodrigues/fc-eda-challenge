FROM golang:1.19

WORKDIR /app
ENV PATH="/go/bin:${PATH}"

RUN apt-get update && apt-get install -y librdkafka-dev

COPY ./ /app/
RUN go build ./cmd/walletcore/main.go
CMD ["./main"]