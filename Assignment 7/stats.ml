(* OCaml implementation for mean, median, and mode *)
let mean lst =
  let sum = List.fold_left ( +. ) 0.0 (List.map float_of_int lst) in
  sum /. float_of_int (List.length lst)

let median lst =
  let sorted = List.sort compare lst in
  let len = List.length sorted in
  if len mod 2 = 0 then
    let mid1 = List.nth sorted (len / 2 - 1) in
    let mid2 = List.nth sorted (len / 2) in
    float_of_int (mid1 + mid2) /. 2.0
  else
    float_of_int (List.nth sorted (len / 2))

let mode lst =
  let table = Hashtbl.create 10 in
  List.iter (fun x ->
    let count = try Hashtbl.find table x with Not_found -> 0 in
    Hashtbl.replace table x (count + 1)
  ) lst;
  let max_val = ref (List.hd lst) in
  let max_count = ref 0 in
  Hashtbl.iter (fun key count ->
    if count > !max_count then (max_count := count; max_val := key)
  ) table;
  !max_val

(* Test the functions *)
let () =
  let numbers = [2; 3; 5; 3; 8; 3; 10] in
  Printf.printf "Mean: %.2f\n" (mean numbers);
  Printf.printf "Median: %.2f\n" (median numbers);
  Printf.printf "Mode: %d\n" (mode numbers)
