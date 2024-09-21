	global	main
	extern	printf

	section	.text

main:

; Expression: IntegerLiteral: 4
;   Evaluate
	mov	rax, 4
;   Print
;     arg[0] (rdi) = formatting string
;     arg[1] (rsi) = expression result
;     arg[2] (rax) = end of varargs
	mov	rdi,	expstr8
	pop	rsi
	mov	rax,	0
	call	printf
; Expression: (+IntegerLiteral: 3 IntegerLiteral: 1)
;   Evaluate
	mov	rax, 3
	push	rax
	mov	rax, 1
	push	rbx
	pop	rbx
	pop	rax
	add	rax, rbx
;   Print
	mov	rdi,	expstr10
	pop	rsi
	mov	rax,	0
	call	printf

; Return from main()
	mov	rax,	0
	ret

	section	.data

expstr8:	db	"IntegerLiteral: 4 = %d", 10, 0
expstr10:	db	"(+IntegerLiteral: 3 IntegerLiteral: 1) = %d", 10, 0
