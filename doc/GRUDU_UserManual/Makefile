#****************************************************************************#
#* GRUDU User Manual Makefile                                               *#
#*                                                                          *#
#* Author(s):                                                               *#
#* - Abdelkader AMAR (Abdelkader.Amar@ens-lyon.fr)                          *#
#* - David LOUREIRO (David.Loureiro@ens-lyon.fr)                            *#
#*                                                                          *#
#* $LICENSE$                                                                *#
#****************************************************************************#
#* $Id: Makefile,v 1.4 2007/10/31 13:48:11 dloureir Exp $
#* $Log: Makefile,v $
#* Revision 1.4  2007/10/31 13:48:11  dloureir
#* Adding the convertion for png -> eps
#*
#* Revision 1.3  2007/10/25 12:03:49  dloureir
#* Adding a build file for the compilation of the User's Manual of GRUDU, and removing the OUTDATED message for eps files when their png counterparts exist.
#*
#* Revision 1.2  2007/07/18 15:32:10  dloureir
#* An enhanced Makefile
#*
#****************************************************************************#

LATEX	= latex
BIBTEX	= bibtex
MAKEINDEX = makeindex
XDVI	= xdvi -gamma 4
XPDF    = xpdf
DVIPS	= dvips
DVIPDF  = dvipdfm
PS2PDF  = ps2pdf
L2H	= latex2html
GH	= gv

RERUN = "(There were undefined references|Rerun to get (cross-references|the bars) right)"
RERUNBIB = "No file.*\.bbl|Citation.*undefined"
MAKEIDX = "^[^%]*\\makeindex"
MPRINT = "^[^%]*print"
USETHUMBS = "^[^%]*thumbpdf"

SRC	:= $(shell egrep -l '^[^%]*\\begin\{document\}' *.tex)
BIBFILE := $(shell perl -ne '($$_)=/^[^%]*\\bibliography\{(.*?)\}/;@_=split /,/;foreach $$b (@_) {print "$$b.bib "}' $(SRC))

EPSPICS := $(shell perl -ne '@foo=/^[^%]*\\(includegraphics|psfig)(\[.*?\])?\{(.*?)\}/g;if (defined($$foo[2])) { if ($$foo[2] =~ /.eps$$/) { print "$$foo[2] "; } else { print "$$foo[2].eps "; }}' *.tex)
DEP     := $(shell perl -ne '($$_)=/^[^%]*\\include\{(.*?)\}/;@_=split /,/;foreach $$t (@_) {print "$$t.tex "}' $(SRC))

TRG	= $(SRC:%.tex=%.dvi)
PSF	= $(SRC:%.tex=%.ps)
PDF	= $(SRC:%.tex=%.pdf)

COPY = if test -r $(<:%.tex=%.toc); then cp $(<:%.tex=%.toc) $(<:%.tex=%.toc.bak); fi 
RM = rm -f
OUTDATED = echo "EPS-file is out-of-date!" && false


all 	: $(TRG)

define run-latex
	  $(COPY);$(LATEX) $<
	  egrep -q $(MAKEIDX) $< && ($(MAKEINDEX) $(<:%.tex=%);$(COPY);$(LATEX) $<) ; true
	  egrep -c $(RERUNBIB) $(<:%.tex=%.log) && ($(BIBTEX) $(<:%.tex=%);$(COPY);$(LATEX) $<) ; true
	  egrep -q $(RERUN) $(<:%.tex=%.log) && ($(COPY);$(LATEX) $<) ; true
	  egrep -q $(RERUN) $(<:%.tex=%.log) && ($(COPY);$(LATEX) $<) ; true
	  if cmp -s $(<:%.tex=%.toc) $(<:%.tex=%.toc.bak); then true ;else $(LATEX) $< ; fi
	  $(RM) $(<:%.tex=%.toc.bak)
	  # Display relevant warnings
	  egrep -i "(Reference|Citation).*undefined" $(<:%.tex=%.log) ; true
endef

$(TRG)	: %.dvi : %.tex $(DEP) $(EPSPICS) $(BIBFILE)
	  @$(run-latex)

$(PSF)	: %.ps : %.dvi
	  @$(DVIPS) $< -o $@

$(PDF)  : %.pdf : %.ps
	  @echo "$(PS2PDF) $< $@"
	  @$(PS2PDF) $< $@

show	: $(TRG)
	  @for i in $(TRG) ; do $(XDVI) $$i & done

showps	: $(PSF)
	  @for i in $(PSF) ; do $(GH) $$i & done

showpdf : $(PDF)
	  @for i in $(PDF) ; do $(XPDF) $$i & done

ps	: $(PSF) 

pdf	: $(PDF) 

# TODO: This probably needs fixing
html	: @$(DEP) $(EPSPICS)
	  @$(L2H) $(SRC)

clean	:
	  -rm -f $(TRG) $(PSF) $(PDF) $(TRG:%.dvi=%.aux) $(TRG:%.dvi=%.bbl) $(TRG:%.dvi=%.blg) $(TRG:%.dvi=%.log) $(TRG:%.dvi=%.out) $(TRG:%.dvi=%.idx) $(TRG:%.dvi=%.ilg) $(TRG:%.dvi=%.ind) $(TRG:%.dvi=%.toc) $(TRG:%.dvi=%.lof) $(DEP:%.tex=%.aux)

.PHONY	: all show clean ps pdf showps


######################################################################
# Define rules for EPS source files.
%.eps: %.sxd
	$(OUTDATED)
%.eps: %.sda
	$(OUTDATED)
%.eps: %.png
	convert $< $@
%.eps: %.sxc
	$(OUTDATED)
%.eps: %.xcf
	$(OUTDATED)
%.eps: %.zargo
	$(OUTDATED)
%.eps: %.m
	@egrep -q $(MPRINT) $< && ($(OUTDATED))
