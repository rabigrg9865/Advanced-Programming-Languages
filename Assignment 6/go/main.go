package main

import (
	"fmt"
	"math/rand"
	"os"
	"sync"
	"time"
)

type RideTask struct {
	id int
}

func (r RideTask) process() {
	start := time.Now()
	delay := time.Duration(1+rand.Intn(3)) * time.Second
	time.Sleep(delay)
	duration := time.Since(start)
	fmt.Printf("Task %d completed in %v\n", r.id, duration)
}

func worker(id int, tasks <-chan *RideTask, wg *sync.WaitGroup, mu *sync.Mutex, logfile *os.File) {
	defer wg.Done()
	for task := range tasks {
		if task == nil {
			log(mu, logfile, fmt.Sprintf("Worker %d exiting.", id))
			return
		}
		log(mu, logfile, fmt.Sprintf("Worker %d started task %d", id, task.id))
		task.process()
		log(mu, logfile, fmt.Sprintf("Worker %d completed task %d", id, task.id))
	}
}

func log(mu *sync.Mutex, file *os.File, message string) {
	timestamp := time.Now().Format(time.RFC3339)
	entry := fmt.Sprintf("[%s] %s\n", timestamp, message)

	mu.Lock()
	defer mu.Unlock()

	fmt.Print(entry)
	file.WriteString(entry)
}

func main() {
	const numWorkers = 4
	const numTasks = 10
	outputFile := "output.txt"

	rand.Seed(time.Now().UnixNano())

	logfile, err := os.Create(outputFile)
	if err != nil {
		fmt.Println("Failed to create output file:", err)
		return
	}
	defer logfile.Close()

	tasks := make(chan *RideTask)
	var wg sync.WaitGroup
	var mu sync.Mutex

	// Start worker goroutines
	for i := 0; i < numWorkers; i++ {
		wg.Add(1)
		go worker(i, tasks, &wg, &mu, logfile)
	}

	// Enqueue ride tasks
	for i := 0; i < numTasks; i++ {
		tasks <- &RideTask{id: i}
	}

	// Send nils to signal shutdown
	for i := 0; i < numWorkers; i++ {
		tasks <- nil
	}

	close(tasks)
	wg.Wait()
}
  
