if (!requireNamespace("BiocManager", quietly = TRUE))
  install.packages("BiocManager")

BiocManager::install("graph")
require(igraph)
require(graph)
require(eulerian)
require(GA)

make_eulerian = function(graph) {
  info = c("broken" = FALSE, "Added" = 0, "Successfull" = TRUE)
  is.even = function(x) { x %% 2 == 0 }
  search.for.even.neighbor = !is.even(sum(!is.even(degree(graph))))
  for (i in V(graph)) {
    set.j = NULL
    uneven.neighbors = !is.even(degree(graph, neighbors(graph, i)))
    if (!is.even(degree(graph, i))) {
      if (sum(uneven.neighbors) == 0) {
        if (sum(!is.even(degree(graph))) > 0) {
          info["Broken"] = TRUE
          uneven.candidates & lt; -!is.even(degree(graph, V(graph)))
          if (sum(uneven.candidates) != 0) {
            set.j & lt; -V(graph)[uneven.candidates][[1]]
          }else {
            info["Successfull"] & lt; -FALSE
          }
        }
      }else {
        set.j & lt; -neighbors(graph, i)[uneven.neighbors][[1]]
      }
    }else if (search.for.even.neighbor == TRUE & is.null(set.j)) {
      info["Added"] & lt; -info["Added"] + 1
      set.j & lt; -neighbors(graph, i)[!uneven.neighbors][[1]]
      if (!is.null(set.j)) { search.for.even.neighbor & lt; -FALSE }
    }
    if (!is.null(set.j)) {
      if (i != set.j) {
        graph & lt; -add_edges(graph, edges = c(i, set.j))
        info["Added"] & lt; -info["Added"] + 1
      }
    }
  }
  (list("graph" = graph, "info" = info)) }

g1 = graph(c(1, 2, 1, 3, 2, 4, 2, 5, 1, 5, 3, 5,
             4, 7, 5, 7, 5, 8, 3, 6, 6, 8, 6, 9, 9, 11, 8, 11,
             8, 10, 8, 12, 7, 10, 10, 12, 11, 12), directed = FALSE)

V(g1)$name = LETTERS[1:12]
V(g1)$color = rgb(0, 0, 1, .4)
ly = layout.kamada.kawai(g1)
plot(g1, layout = ly)

eulerian = make_eulerian(g1)
eulerian$info
g = eulerian$graph

ly = layout.kamada.kawai(g)
plot(g, layout = ly)

A <- as.matrix(as_adj(g))
A1 <- as.matrix(as_adj(g1))
newA = lower.tri(A, diag = FALSE) * A1 + upper.tri(A, diag = FALSE) * A
for (i in 1:sum(newA == 2)) newA = cbind(newA, 0)
for (i in 1:sum(newA == 2)) newA = rbind(newA, 0)
s = nrow(A)
for (i in 1:nrow(A)) {
  Aj = which(newA[i,] == 2)
  if (!is.null(Aj)) {
    for (j in Aj) {
      newA[i, s + 1] = newA[s + 1, i] = 1
      newA[j, s + 1] = newA[s + 1, j] = 1
      newA[i, j] = 1
      s = s + 1
    } } }

newg = graph_from_adjacency_matrix(newA)
newg = as.undirected(newg)
V(newg)$name = LETTERS[1:17]
V(newg)$color = c(rep(rgb(0, 0, 1, .4), 12), rep(rgb(1, 0, 0, .4), 5))
ly2 = ly
transl = cbind(c(0, 0, 0, .2, 0), c(.2, -.2, -.2, 0, -.2))
for (i in 13:17) {
  j = which(newA[i,] > 0)
  lc = ly[j,]
  ly2 = rbind(ly2, apply(lc, 2, mean) + transl[i - 12,])
}
plot(newg, layout = ly2)

plot(newg, vertex.color = V(newg)$color, layout = ly2,
     vertex.size = c(rep(20, 12), rep(0, 5)),
     vertex.label.cex = c(rep(1, 12), rep(.1, 5)))


edg = attr(E(newg), "vnames")
ET = PairViz::etour(g1, weighted = FALSE)
parcours = trajet = rep(NA, length(ET) - 1)
for (i in 1:length(parcours)) {
  u = c(ET[i], ET[i + 1])
  ou = order(u)
  parcours[i] = paste(u[ou[1]], u[ou[2]], sep = "|")
  trajet[i] = which(edg == parcours[i])
}

